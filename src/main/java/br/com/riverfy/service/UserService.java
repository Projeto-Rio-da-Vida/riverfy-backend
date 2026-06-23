package br.com.riverfy.service;

import br.com.riverfy.dto.user.UserRequest;
import br.com.riverfy.dto.user.UserResponse;
import br.com.riverfy.exception.ResourceNotFoundException;
import br.com.riverfy.model.Category;
import br.com.riverfy.model.User;
import br.com.riverfy.model.enums.UserStatus;
import br.com.riverfy.repository.CategoryRepository;
import br.com.riverfy.repository.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repository;
    private final CategoryRepository categoryRepository;

    public UserService(UserRepository repository, CategoryRepository categoryRepository) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public UserResponse update(Long id, UserRequest request) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        if (!user.getEmail().equalsIgnoreCase(request.email())) {
            repository.findByEmail(request.email()).ifPresent(u -> {
                throw new IllegalArgumentException("This email is already in use by another user.");
            });
        }

        user.setName(request.name());
        user.setEmail(request.email());

        List<Category> updatedCategories = categoryRepository.findAllById(request.categoryIds());

        if (updatedCategories.size() != request.categoryIds().size()) {
            throw new ResourceNotFoundException("One or more category IDs were not found or are inactive.");
        }

        boolean hasDefaultCategory = updatedCategories.stream().anyMatch(c -> c.getId() == 1L);
        if (!hasDefaultCategory) {
            Category defaultCategory = categoryRepository.findByIdAndActiveTrue(1L)
                    .orElseThrow(() -> new ResourceNotFoundException("Default category (ID 1) not found."));

            updatedCategories = new java.util.ArrayList<>(updatedCategories);
            updatedCategories.add(defaultCategory);
        }

        user.setCategories(updatedCategories);

        User updatedUser = repository.save(user);

        return br.com.riverfy.dto.user.UserResponse.fromEntity(updatedUser);
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

    public long countActiveMembers() {
        return repository.countByStatus(UserStatus.ACTIVE);
    }


    public  List<User> getRecentMembers() {
        return this.repository.findTop5ByOrderByCreatedAtDesc();
    }
}