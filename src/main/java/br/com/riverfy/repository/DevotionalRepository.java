package br.com.riverfy.repository;

import br.com.riverfy.model.Devotional;
import br.com.riverfy.model.enums.DevotionalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DevotionalRepository extends JpaRepository<Devotional, Long> {

    @Query("SELECT d FROM Devotional d WHERE d.id = :id AND d.active = true")
    Optional<Devotional> findByIdAndActiveTrue(
            @Param("id") Long id
    );

    @Query("SELECT d FROM Devotional d WHERE d.active = true " +
            "AND (:status IS NULL OR d.status = :status) " +
            "AND LOWER(d.title) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Devotional> searchActiveDevotionals(
            @Param("searchTerm") String searchTerm,
            @Param("status") DevotionalStatus status,
            Pageable pageable
    );
}
