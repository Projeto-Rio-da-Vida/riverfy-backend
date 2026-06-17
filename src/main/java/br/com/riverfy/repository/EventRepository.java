package br.com.riverfy.repository;

import br.com.riverfy.model.Event;
import br.com.riverfy.model.enums.EventStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT e FROM Event e WHERE e.active = true " +
            "AND (:status IS NULL OR e.eventStatus = :status) " +
            "AND (LOWER(e.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(e.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<Event> searchActiveEvents(
            @Param("searchTerm") String searchTerm,
            @Param("status") EventStatus status,
            Pageable pageable
    );

    @Query("SELECT e FROM Event e WHERE e.id = :id AND e.active = true")
    Optional<Event> findByIdAndActiveTrue(@Param("id") Long id);
}
