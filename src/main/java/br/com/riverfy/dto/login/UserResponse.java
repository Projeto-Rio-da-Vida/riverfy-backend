package br.com.riverfy.dto.login;

import br.com.riverfy.model.enums.UserRole;
import br.com.riverfy.model.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {

    private Long id;

    private String name;

    private String email;

    private UserRole role;

    private UserStatus status;
}