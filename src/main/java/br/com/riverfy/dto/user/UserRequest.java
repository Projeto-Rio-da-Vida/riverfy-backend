package br.com.riverfy.dto.user;

import br.com.riverfy.model.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record UserRequest(
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        UserRole role,

        @NotEmpty(message = "The category IDs list cannot be empty")
        List<Long> categoryIds
) {
}
