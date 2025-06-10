package com.garage.qr.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QrCodeRepository extends JpaRepository<QrCodeEntity, Long> {

    Optional<QrCodeEntity> findByName(String name);

    List<QrCodeEntity> findByToolId(Long toolId);

    @Modifying
    @Query("DELETE FROM QrCodeEntity q WHERE q.tool.id = :toolId")
    void deleteByToolId(@Param("toolId") Long toolId);
}
