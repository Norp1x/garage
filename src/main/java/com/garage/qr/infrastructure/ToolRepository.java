package com.garage.qr.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ToolRepository extends JpaRepository<ToolEntity, Long> {

    Optional<ToolEntity> findByName(String name);

    List<ToolEntity> findByTypeIgnoreCase(String type);

    @Query("""
            SELECT t FROM ToolEntity t WHERE
            LOWER(t.name) LIKE LOWER(CONCAT('%', :search, '%')) OR
            LOWER(t.type) LIKE LOWER(CONCAT('%', :search, '%')) OR
            LOWER(t.description) LIKE LOWER(CONCAT('%', :search, '%'))
            """)
    List<ToolEntity> searchTools(@Param("search") String search);

    List<ToolEntity> findByToolPlacingIgnoreCase(String toolPlacing);

    @Query("SELECT COUNT(t) FROM ToolEntity t WHERE t.quantity < :threshold")
    long countLowStockTools(@Param("threshold") Integer threshold);
}
