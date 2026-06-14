package com.lmello.titer.users.internal.services;

import com.lmello.titer.shared.api.jwt.JwtService;
import com.lmello.titer.users.dto.AuthResponse;
import com.lmello.titer.users.dto.UserResponse;
import com.lmello.titer.users.entities.RoleEntity;
import com.lmello.titer.users.entities.UserEntity;
import com.lmello.titer.users.mapper.AuthMapper;
import com.lmello.titer.users.mapper.UserMapper;
import com.lmello.titer.users.services.AuthResponseFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthResponseFactoryTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private AuthMapper authMapper;

    @InjectMocks
    private AuthResponseFactory authResponseFactory;

    @Test
    void createsJwtBackedAuthResponse() {
        UserEntity user = UserEntity.builder()
                .id(UUID.randomUUID())
                .username("alice")
                .email("alice@example.com")
                .roles(Set.of(RoleEntity.builder().name("USER").build()))
                .build();
        Set<String> authorities = Set.of("USER");
        UserResponse userResponse = new UserResponse(user.getId(), user.getEmail(), user.getUsername(), null, null, null);

        when(authMapper.toAuthorities(user)).thenReturn(authorities);
        when(jwtService.generateToken(user.getId(), user.getUsername(), user.getEmail(), authorities)).thenReturn("jwt");
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        AuthResponse response = authResponseFactory.create(user);

        assertThat(response.accessToken()).isEqualTo("jwt");
        assertThat(response.user()).isSameAs(userResponse);
    }
}
