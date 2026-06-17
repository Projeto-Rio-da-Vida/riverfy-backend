package br.com.riverfy.repository;

import br.com.riverfy.model.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {
    @Query("SELECT n FROM Notice n WHERE n.id = :id AND n.active = true")
    Optional<Notice> findByIdAndActiveTrue(
            @Param("id") Long id
    );

    @Query("SELECT n FROM Notice n WHERE n.active = true AND " +
            "(LOWER(n.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(n.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<Notice> searchActiveNotices(
            @Param("searchTerm") String searchTerm,
            Pageable pageable
    );
}
