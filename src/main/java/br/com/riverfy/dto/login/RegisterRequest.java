package br.com.riverfy.dto.login;

import br.com.riverfy.model.enums.UserRole;

public record RegisterRequest(String name, String email, String password, UserRole role) { }
