package com.lmello.titer.user;

import com.lmello.titer.user.internal.web.dto.CreateUserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {

    Page<UserDTO> getAllUsers(Pageable pageable);

    UserDTO getUserById(UUID id);

    UserDTO create(CreateUserDTO req);
}
