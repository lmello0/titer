package com.lmello.titer.users.internal.services;

import com.lmello.titer.users.api.LoginRequest;
import com.lmello.titer.users.internal.entities.UserAuthEntity;
import com.lmello.titer.users.internal.entities.UserAuthId;
import com.lmello.titer.users.internal.entities.UserEntity;
import com.lmello.titer.users.internal.enums.AuthProvider;
import com.lmello.titer.users.internal.exception.InvalidCredentialsException;
import com.lmello.titer.users.internal.repositories.UserAuthRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocalCredentialServiceTest {

    @Mock
    private UserAuthRepository authRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private LocalCredentialService localCredentialService;

    private static UserEntity user() {
        return UserEntity.builder()
                .id(UUID.randomUUID())
                .username("alice")
                .email("alice@example.com")
                .build();
    }

    private static UserAuthEntity localAuth(UserEntity user) {
        return UserAuthEntity.builder()
                .id(new UserAuthId(user.getId(), AuthProvider.LOCAL))
                .user(user)
                .passwordHash("hash")
                .build();
    }

    @Test
    void createHashesPasswordAndSavesLocalAuth() {
        UserEntity user = user();

        when(passwordEncoder.encode("P@ssw0rd!")).thenReturn("hash");

        localCredentialService.create(user, "P@ssw0rd!");

        ArgumentCaptor<UserAuthEntity> captor = ArgumentCaptor.forClass(UserAuthEntity.class);
        verify(authRepository).save(captor.capture());

        UserAuthEntity saved = captor.getValue();
        assertThat(saved.getUser()).isSameAs(user);
        assertThat(saved.getPasswordHash()).isEqualTo("hash");
        assertThat(saved.getId()).isEqualTo(new UserAuthId(user.getId(), AuthProvider.LOCAL));
    }

    @Test
    void authenticateFindsLocalUserByUsername() {
        LoginRequest request = new LoginRequest("alice", "P@ssw0rd!");
        UserAuthEntity auth = localAuth(user());

        when(authRepository.findByUserUsernameAndIdProvider("alice", AuthProvider.LOCAL)).thenReturn(Optional.of(auth));
        when(passwordEncoder.matches("P@ssw0rd!", "hash")).thenReturn(true);

        UserEntity result = localCredentialService.authenticate(request);

        assertThat(result).isSameAs(auth.getUser());
        verify(authRepository, never()).findByUserEmailAndIdProvider("alice", AuthProvider.LOCAL);
    }

    @Test
    void authenticateFallsBackToEmail() {
        LoginRequest request = new LoginRequest("alice@example.com", "P@ssw0rd!");
        UserAuthEntity auth = localAuth(user());

        when(authRepository.findByUserUsernameAndIdProvider("alice@example.com", AuthProvider.LOCAL))
                .thenReturn(Optional.empty());
        when(authRepository.findByUserEmailAndIdProvider("alice@example.com", AuthProvider.LOCAL))
                .thenReturn(Optional.of(auth));
        when(passwordEncoder.matches("P@ssw0rd!", "hash")).thenReturn(true);

        UserEntity result = localCredentialService.authenticate(request);

        assertThat(result).isSameAs(auth.getUser());
    }

    @Test
    void authenticateRejectsMissingAuth() {
        LoginRequest request = new LoginRequest("alice", "P@ssw0rd!");

        when(authRepository.findByUserUsernameAndIdProvider("alice", AuthProvider.LOCAL)).thenReturn(Optional.empty());
        when(authRepository.findByUserEmailAndIdProvider("alice", AuthProvider.LOCAL)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> localCredentialService.authenticate(request))
                .isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    void authenticateRejectsBadPassword() {
        LoginRequest request = new LoginRequest("alice", "wrong");
        UserAuthEntity auth = localAuth(user());

        when(authRepository.findByUserUsernameAndIdProvider("alice", AuthProvider.LOCAL)).thenReturn(Optional.of(auth));
        when(passwordEncoder.matches("wrong", "hash")).thenReturn(false);

        assertThatThrownBy(() -> localCredentialService.authenticate(request))
                .isInstanceOf(InvalidCredentialsException.class);
    }
}
