package com.lmello.titer.users.internal.services;

import com.lmello.titer.storage.api.FileService;
import com.lmello.titer.users.api.RegisterRequest;
import com.lmello.titer.users.internal.entities.RoleEntity;
import com.lmello.titer.users.internal.entities.UserEntity;
import com.lmello.titer.users.internal.entities.UserRoleAuditEntity;
import com.lmello.titer.users.internal.enums.Role;
import com.lmello.titer.users.internal.enums.UserRoleAuditAction;
import com.lmello.titer.users.internal.exception.DuplicateUserException;
import com.lmello.titer.users.internal.repositories.RoleRepository;
import com.lmello.titer.users.internal.repositories.UserRepository;
import com.lmello.titer.users.internal.repositories.UserRoleAuditRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRegistrationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRoleAuditRepository userRoleAuditRepository;

    @Mock
    private FileService fileService;

    @InjectMocks
    private UserRegistrationService userRegistrationService;

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

    @Test
    void createLocalUserAlwaysGrantsDefaultUserRole() {
        RegisterRequest request = registerRequest();
        RoleEntity role = RoleEntity.builder().id(1L).name(Role.USER.name()).build();

        when(userRepository.existsByUsernameOrEmail(request.username(), request.email())).thenReturn(false);
        when(roleRepository.findByName(Role.USER.name())).thenReturn(Optional.of(role));
        when(userRepository.saveAndFlush(any(UserEntity.class))).thenAnswer(invocation -> {
            UserEntity user = invocation.getArgument(0);
            user.setId(UUID.randomUUID());
            return user;
        });

        UserEntity result = userRegistrationService.createLocalUser(request);

        assertThat(result.getUsername()).isEqualTo(request.username());
        assertThat(result.getEmail()).isEqualTo(request.email());
        assertThat(result.getRoles()).containsExactly(role);

        ArgumentCaptor<UserRoleAuditEntity> auditCaptor = ArgumentCaptor.forClass(UserRoleAuditEntity.class);
        verify(userRoleAuditRepository).save(auditCaptor.capture());

        UserRoleAuditEntity audit = auditCaptor.getValue();
        assertThat(audit.getUser()).isSameAs(result);
        assertThat(audit.getRole()).isSameAs(role);
        assertThat(audit.getAction()).isEqualTo(UserRoleAuditAction.GRANTED);
        assertThat(audit.getPerformedBy()).isEqualTo(result.getId().toString());
    }

    @Test
    void createLocalUserRejectsDuplicateBeforeRoleLookupOrSave() {
        RegisterRequest request = registerRequest();

        when(userRepository.existsByUsernameOrEmail(request.username(), request.email())).thenReturn(true);

        assertThatThrownBy(() -> userRegistrationService.createLocalUser(request))
                .isInstanceOf(DuplicateUserException.class);

        verify(roleRepository, never()).findByName(any());
        verify(userRepository, never()).saveAndFlush(any());
        verify(userRoleAuditRepository, never()).save(any());
    }
}
