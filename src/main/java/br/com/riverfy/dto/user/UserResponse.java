package br.com.riverfy.dto.user;

import br.com.riverfy.model.User;

public record UserResponse(
        Long id,
        String name,
        String email
) {
    public static UserResponse fromEntity(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }
}
