package com.lmello.titer.users.service;

import com.lmello.titer.users.api.UserService;
import com.lmello.titer.users.api.command.CreateUserCommand;
import com.lmello.titer.users.api.events.UserCreatedEvent;
import com.lmello.titer.users.api.events.UserDeletedEvent;
import com.lmello.titer.users.api.events.UserEmailVerifiedEvent;
import com.lmello.titer.users.api.events.UserPatchedEvent;
import com.lmello.titer.users.api.representation.UserInfo;
import com.lmello.titer.users.entities.RoleEntity;
import com.lmello.titer.users.entities.UserEntity;
import com.lmello.titer.users.enums.Role;
import com.lmello.titer.users.exceptions.EmailAlreadyExistsException;
import com.lmello.titer.users.exceptions.RoleNotFoundException;
import com.lmello.titer.users.exceptions.UserNotFoundException;
import com.lmello.titer.users.exceptions.UsernameAlreadyExistsException;
import com.lmello.titer.users.mappers.UserMapper;
import com.lmello.titer.users.repositories.RoleRepository;
import com.lmello.titer.users.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

// TODO: add specification

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional(readOnly = true)
    public Page<UserInfo> getAll(Pageable pageable) {
        return userRepository
                .findAll(pageable)
                .map(userMapper::toUserInfo);
    }

    @Override
    @Transactional
    public UserInfo patch(UUID id, UserPatch patch) {
        UserEntity u = userRepository
                .findById(id)
                .orElseThrow(UserNotFoundException::new);

        if (patch.username() != null && !u.getUsername().equalsIgnoreCase(patch.username())) {
            if (userRepository.existsByUsername(patch.username())) {
                throw new UsernameAlreadyExistsException(patch.username());
            }

            u.setUsername(patch.username());
        }

        if (patch.firstName() != null) {
            u.setFirstName(patch.firstName());
        }

        if (patch.lastName() != null) {
            u.setLastName(patch.lastName());
        }

        u.setModifiedAt(Instant.now());
        u.setModifiedBy("system");

        UserInfo modifiedUserData = userMapper.toUserInfo(userRepository.save(u));

        eventPublisher.publishEvent(new UserPatchedEvent(modifiedUserData));

        return modifiedUserData;
    }

    @Override
    @Transactional
    public void softDelete(UUID id, String deletedBy) {
        UserEntity u = userRepository
                .findById(id)
                .orElseThrow(UserNotFoundException::new);

        u.setDeletedAt(Instant.now());
        u.setDeletedBy(deletedBy);

        eventPublisher.publishEvent(new UserDeletedEvent(id));
    }

    @Override
    @Transactional
    public UserInfo create(CreateUserCommand command) {
        if (userRepository.existsByUsername(command.username())) {
            throw new UsernameAlreadyExistsException(command.username());
        }

        if (userRepository.existsByEmail(command.email())) {
            throw new EmailAlreadyExistsException(command.email());
        }

        RoleEntity defaultRole = roleRepository.findByName(Role.USER.name())
                .orElseThrow(() -> new RoleNotFoundException(Role.USER));

        UserEntity u = UserEntity.builder()
                .username(command.username())
                .email(command.email())
                .firstName(command.firstName())
                .lastName(command.lastName())
                .profilePicture(null)
                .isEmailVerified(false)
                .build();

        u.addRole(defaultRole);

        u.setCreatedBy(command.createdBy());
        u.setCreatedAt(Instant.now());
        u.setModifiedBy(command.createdBy());
        u.setModifiedAt(Instant.now());

        UserInfo createdUser = userMapper.toUserInfo(userRepository.save(u));

        eventPublisher.publishEvent(new UserCreatedEvent(createdUser));

        return createdUser;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserInfo> findByEmail(String email) {
        return userRepository
                .findByEmail(email)
                .map(userMapper::toUserInfo);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserInfo> findByUsername(String username) {
        return userRepository
                .findByUsername(username)
                .map(userMapper::toUserInfo);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserInfo> findByUsernameOrEmail(String identifier) {
        return userRepository
                .findByUsernameOrEmail(identifier)
                .map(userMapper::toUserInfo);
    }

    @Override
    @Transactional
    public void markEmailVerified(UUID id) {
        UserEntity u = userRepository
                .findById(id)
                .orElseThrow(UserNotFoundException::new);

        u.setEmailVerified(true);

        u.setModifiedAt(Instant.now());
        u.setModifiedBy(id.toString());

        eventPublisher.publishEvent(new UserEmailVerifiedEvent(id));
    }

    @Override
    public Optional<UserInfo> findById(UUID userId) {
        return userRepository
                .findById(userId)
                .map(userMapper::toUserInfo);
    }
}
