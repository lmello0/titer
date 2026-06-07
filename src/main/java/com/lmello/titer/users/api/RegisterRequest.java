package com.lmello.titer.users.api;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record RegisterRequest(
        @NotBlank
        @Pattern(regexp = "^[a-zA-Z0-9_]+$")
        @Size(min = 3, max = 20)
        String username,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{8,72}$",
                message = "Password must contain at least one uppercase letter, one lowercase, one number and one special character"
        )
        @Size(min = 8, max = 72)
        String password,

        @Pattern(regexp = "^[a-zA-Z ]+$")
        @Size(min = 3, max = 100)
        String firstName,

        @Pattern(regexp = "^[a-zA-Z ]+$")
        @Size(max = 100)
        String lastName,

        MultipartFile profilePicture
) {
}
