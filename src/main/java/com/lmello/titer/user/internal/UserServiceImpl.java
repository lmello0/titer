package com.lmello.titer.user.internal;

import com.lmello.titer.user.UserDTO;
import com.lmello.titer.user.UserService;
import com.lmello.titer.user.internal.web.dto.CreateUserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toDTO);
    }

    @Override
    public UserDTO getUserById(UUID id) {
        return userRepository.findById(id)
                .map(userMapper::toDTO)
                .orElseThrow();
    }

    @Override
    public UserDTO create(CreateUserDTO req) {
        User newUser = userMapper.toEntity(req);

        return userMapper.toDTO(userRepository.save(newUser));
    }
}
