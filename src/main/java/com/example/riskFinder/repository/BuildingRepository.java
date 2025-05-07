package com.example.riskFinder.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.riskFinder.model.Building;

public interface BuildingRepository extends JpaRepository<Building, Long> {

    /** 반경 radius 미터 안에서 가장 가까운 건물 1개 */
    @Query(value = """
        SELECT *
        FROM building
        WHERE ST_Distance_Sphere(location,
              ST_GeomFromText(CONCAT('POINT(', :lon, ' ', :lat, ')'), 4326)
        ) < :radius
        ORDER BY ST_Distance_Sphere(location,
              ST_GeomFromText(CONCAT('POINT(', :lon, ' ', :lat, ')'), 4326)
        )
        LIMIT 1
        """, nativeQuery = true)
    Optional<Building> findNearest(double lat, double lon, int radius);
}
