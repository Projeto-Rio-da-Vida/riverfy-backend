package br.com.riverfy.repository;

import br.com.riverfy.model.User;
import br.com.riverfy.model.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("""
        SELECT u FROM User u
        WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :search, '%'))
           OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))
    """)
    Page<User> searchByNameOrEmail(@Param("search") String search, Pageable pageable);

    long countByStatus(UserStatus status);

    List<User> findTop5ByOrderByCreatedAtDesc();
}
