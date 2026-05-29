package br.com.riverfy.service;

import br.com.riverfy.dto.login.UserResponse;
import br.com.riverfy.model.User;
import br.com.riverfy.model.enums.UserStatus;
import br.com.riverfy.repository.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public Page<User> findAll(String search, Pageable pageable) {

        if (search == null || search.isBlank()) {
            return repository.findAll(pageable);
        }

        return repository.searchByNameOrEmail(search, pageable);
    }

    public void deactivateUser(Long id) {

        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setStatus(UserStatus.INACTIVE);

        repository.save(user);
    }

    public void activateUser(Long id) {

        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setStatus(UserStatus.ACTIVE);

        repository.save(user);
    }

    public void deleteUser(Long id) {

        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setStatus(UserStatus.INACTIVE);

        repository.save(user);
    }
}