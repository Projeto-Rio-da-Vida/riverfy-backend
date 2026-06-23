package br.com.riverfy.dto.user;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record UserCategoriesRequest(
        @NotEmpty(message = "The category IDs list cannot be empty")
        List<Long> categoryIds
) {
}
