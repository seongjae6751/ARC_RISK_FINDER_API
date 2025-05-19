package com.example.riskFinder.model;

import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.Point;
import java.time.LocalDateTime;

@Entity
@Table(name = "building",
    indexes = @Index(name = "idx_building_location", columnList = "location"))
@Getter @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
public class Building {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @JdbcTypeCode(SqlTypes.GEOMETRY)
    /** 4326 SRID(경위도) POINT, MySQL 8 + JTS */
    @Column(columnDefinition = "POINT SRID 4326", nullable = false)
    private Point location;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate void preUpdate() { updatedAt = LocalDateTime.now(); }
}
