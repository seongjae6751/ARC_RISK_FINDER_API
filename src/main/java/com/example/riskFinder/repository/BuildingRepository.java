package com.example.riskFinder.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.riskFinder.model.Building;

public interface BuildingRepository extends JpaRepository<Building, Long> {

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
    Optional<Building> findNearestByLonLat(
        @Param("lat") double lat,
        @Param("lon") double lon,
        @Param("radius") int radius
    );
}
