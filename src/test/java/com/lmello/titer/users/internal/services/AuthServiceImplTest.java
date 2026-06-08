package com.lmello.titer.users.internal.services;

import com.lmello.titer.users.api.AuthResponse;
import com.lmello.titer.users.api.LoginRequest;
import com.lmello.titer.users.api.RegisterRequest;
import com.lmello.titer.users.api.UserResponse;
import com.lmello.titer.users.internal.entities.UserEntity;
import com.lmello.titer.users.internal.exception.DuplicateUserException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRegistrationService userRegistrationService;

    @Mock
    private LocalCredentialService localCredentialService;

    @Mock
    private AuthResponseFactory authResponseFactory;

    @InjectMocks
    private AuthServiceImpl authService;

    private static RegisterRequest registerRequest() {
        return new RegisterRequest(
                "alice",
                "alice@example.com",
                "P@ssw0rd!",
                "Alice",
                "Example",
                null
        );
    }

    private static UserEntity user() {
        return UserEntity.builder()
                .id(UUID.randomUUID())
                .username("alice")
                .email("alice@example.com")
                .build();
    }

    private static AuthResponse authResponse(UserEntity user) {
        return new AuthResponse(
                "jwt",
                new UserResponse(
                        user.getId(),
                        user.getEmail(),
                        user.getUsername(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getProfilePicture() == null ? null : "/files/" + user.getProfilePicture()
                )
        );
    }

    @Test
    void registerCreatesUserCredentialsAndResponse() {
        RegisterRequest request = registerRequest();
        UserEntity user = user();
        AuthResponse response = authResponse(user);

        when(userRegistrationService.createLocalUser(request)).thenReturn(user);
        when(authResponseFactory.create(user)).thenReturn(response);

        AuthResponse result = authService.register(request);

        assertThat(result).isSameAs(response);

        InOrder inOrder = inOrder(userRegistrationService, localCredentialService, authResponseFactory);
        inOrder.verify(userRegistrationService).createLocalUser(request);
        inOrder.verify(localCredentialService).create(user, request.password());
        inOrder.verify(authResponseFactory).create(user);
    }

    @Test
    void registerDoesNotCreateCredentialsWhenUserAlreadyExists() {
        RegisterRequest request = registerRequest();

        when(userRegistrationService.createLocalUser(request)).thenThrow(new DuplicateUserException());

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(DuplicateUserException.class);

        verifyNoInteractions(localCredentialService, authResponseFactory);
    }

    @Test
    void loginAuthenticatesLocalCredentialsAndReturnsResponse() {
        LoginRequest request = new LoginRequest("alice", "P@ssw0rd!");
        UserEntity user = user();
        AuthResponse response = authResponse(user);

        when(localCredentialService.authenticate(request)).thenReturn(user);
        when(authResponseFactory.create(user)).thenReturn(response);

        AuthResponse result = authService.login(request);

        assertThat(result).isSameAs(response);

        InOrder inOrder = inOrder(localCredentialService, authResponseFactory);
        inOrder.verify(localCredentialService).authenticate(request);
        inOrder.verify(authResponseFactory).create(user);
    }
}
