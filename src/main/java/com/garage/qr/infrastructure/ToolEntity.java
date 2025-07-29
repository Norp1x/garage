package com.garage.qr.infrastructure;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "tools")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ToolEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 30)
    private String type;

    @Column(length = 200)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private ToolSize size;

    @Column(length = 20)
    private String color;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "tool_placing", nullable = false, length = 100)
    private String toolPlacing;

    @OneToMany(mappedBy = "tool", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QrCodeEntity> qrCodes = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

