package com.garage.qr.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToolJpaRepository extends JpaRepository<ToolEntity, Long> {

    List<ToolEntity> findByTypeIgnoreCase(String type);

    List<ToolEntity> findByToolPlacingIgnoreCase(String toolPlacing);

    @Query("""
            SELECT t FROM ToolEntity t WHERE
            LOWER(t.name) LIKE LOWER(CONCAT('%', :search, '%')) OR
            LOWER(t.type) LIKE LOWER(CONCAT('%', :search, '%')) OR
            LOWER(t.description) LIKE LOWER(CONCAT('%', :search, '%'))
            """)
    List<ToolEntity> searchTools(@Param("search") String search);

    @Query("SELECT COUNT(t) FROM ToolEntity t WHERE t.quantity < :threshold")
    long countLowStockTools(@Param("threshold") Integer threshold);

    ToolEntity findByName(String name);
}
