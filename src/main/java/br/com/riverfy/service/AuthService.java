package br.com.riverfy.service;

import br.com.riverfy.dto.login.LoginRequest;
import br.com.riverfy.dto.login.RegisterRequest;
import br.com.riverfy.dto.login.TokenResponse;
import br.com.riverfy.exception.ResourceNotFoundException;
import br.com.riverfy.model.Category;
import br.com.riverfy.model.User;
import br.com.riverfy.repository.CategoryRepository;
import br.com.riverfy.repository.UserRepository;
import br.com.riverfy.secutiry.CustomUserDetails;
import br.com.riverfy.secutiry.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService, UserRepository userRepository, CategoryRepository categoryRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public TokenResponse login(LoginRequest loginRequest) {

        var authRequest = new UsernamePasswordAuthenticationToken(
                loginRequest.email(),
                loginRequest.password()
        );

        var auth = authenticationManager.authenticate(authRequest);

        CustomUserDetails userDetails =
                (CustomUserDetails) auth.getPrincipal();

        User authenticatedUser = userDetails.getUser();

        String token = jwtService.generateToken(authenticatedUser);

        return new TokenResponse(token);
    }

    @Transactional
    public void register (RegisterRequest registerRequest) {
        Optional<User> existingUser = userRepository.findByEmail(registerRequest.email());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("This email is already in use.");
        }

        Category defaultCategory = categoryRepository.findByIdAndActiveTrue(1L)
                .orElseThrow(() -> new ResourceNotFoundException("Default category (ID 1) not found in database."));

        String encryptedPassword = passwordEncoder.encode(registerRequest.password());

        User newUser = new User(
                registerRequest.name(),
                registerRequest.email(),
                encryptedPassword
        );

        if (registerRequest.role() != null) {
            newUser.setRole(registerRequest.role());
        }

        List<Category> userCategories = new ArrayList<>();
        userCategories.add(defaultCategory);
        newUser.setCategories(userCategories);

        userRepository.save(newUser);
    }
}
