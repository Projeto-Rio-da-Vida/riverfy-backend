package br.com.riverfy.repository;

import br.com.riverfy.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c FROM Category c " +
            "WHERE c.id = :id AND c.active = true"
    )
    Optional<Category> findByIdAndActiveTrue(
            @Param("id") Long id
    );

    @Query("SELECT c FROM Category c " +
            "WHERE c.active = true AND LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))"
    )
    Page<Category> searchActiveCategories(
            @Param("searchTerm") String searchTerm,
            Pageable pageable
    );
}
