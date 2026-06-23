package br.com.riverfy.dto.user;

import br.com.riverfy.dto.category.CategoryResponse;
import br.com.riverfy.model.User;

import java.util.ArrayList;
import java.util.List;

public record UserResponse(
        Long id,
        String name,
        String email,
        List<CategoryResponse> categories
) {
    public static UserResponse fromEntity(User user) {
        List<CategoryResponse> categoryDTOs = new ArrayList<>();
        if (user.getCategories() != null) {
            categoryDTOs = user.getCategories().stream()
                    .map(CategoryResponse::fromEntity)
                    .toList();
        }

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                categoryDTOs
        );
    }
}
