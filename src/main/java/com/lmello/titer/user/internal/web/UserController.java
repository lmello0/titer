package com.lmello.titer.user.internal.web;

import com.lmello.titer.user.UserDTO;
import com.lmello.titer.user.UserService;
import com.lmello.titer.user.internal.web.dto.CreateUserDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
class UserController {

    private final UserService service;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<PagedModel<UserDTO>> getAllUsers(@PageableDefault Pageable pageable) {
        Page<UserDTO> page = service.getAllUsers(pageable);

        return ResponseEntity.ok(new PagedModel<>(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getUserById(id));
    }

    @PostMapping()
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid CreateUserDTO body) {
        UserDTO u = userService.create(body);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(u.id())
                .toUri();

        return ResponseEntity.created(location).body(u);
    }
}
