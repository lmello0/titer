package com.lmello.titer.users.internal.services;

import com.lmello.titer.users.UserFacade;
import com.lmello.titer.users.api.UpdateUserRequest;
import com.lmello.titer.users.api.UserResponse;
import com.lmello.titer.users.events.UserDeletedEvent;
import com.lmello.titer.users.events.UserUpdatedEvent;
import com.lmello.titer.users.internal.entities.UserEntity;
import com.lmello.titer.users.internal.mapper.UserMapper;
import com.lmello.titer.users.internal.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserFacade {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher events;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUser(UUID id) {
        return userRepository.findById(id)
                .map(userMapper::toResponse)
                // TODO: throw domain exception
                .orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    @Transactional
    public UserResponse update(UUID id, UpdateUserRequest request) {
        UserEntity u = userRepository.findById(id)
                // TODO: throw domain exception
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        if (request.username() != null) u.setUsername(request.username());
        if (request.email() != null) u.setEmail(request.email());
        if (request.name() != null) u.setName(request.name());
        if (request.profilePicture() != null) u.setProfilePicture(request.profilePicture());

        events.publishEvent(new UserUpdatedEvent(u.getId()));

        return userMapper.toResponse(u);
    }

    @Transactional
    public void delete(UUID id) {
        // TODO: add soft delete
        if (!userRepository.existsById(id)) {
            // TODO: throw domain exception
            throw new NoSuchElementException("User not found");
        }

        userRepository.deleteById(id);
        events.publishEvent(new UserDeletedEvent(id));
    }
}
