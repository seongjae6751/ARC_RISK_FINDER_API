package com.example.riskFinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.riskFinder.dto.BuildingCrackCountDto;
import com.example.riskFinder.dto.BuildingCrackWidthDto;
import com.example.riskFinder.model.Crack;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public interface CrackRepository extends JpaRepository<Crack, Long> {

    @Query("SELECT MAX(c.detectedAt) FROM Crack c WHERE c.crackId = :crackId")
    LocalDate findLatestDetectionDate(String crackId);

    List<Crack> findByCrackId(String crackId);

    @Query(value = """
    SELECT w.building_id AS buildingId, COUNT(c.id) AS crackCount
    FROM waypoint w
    JOIN crack c ON 
      ABS(c.latitude - w.latitude) < 0.00001 AND
      ABS(c.longitude - w.longitude) < 0.00001 AND
      ABS(c.altitude - w.altitude) < 0.1
    GROUP BY w.building_id
    ORDER BY crackCount DESC
    """, nativeQuery = true)
    List<BuildingCrackCountDto> getCrackCountPerBuilding();

    @Query(value = """
    SELECT w.building_id AS buildingId, MAX(cm.width_mm) AS maxWidth
    FROM crack_measurement cm
    JOIN crack c ON cm.crack_id = c.crack_id
    JOIN waypoint w ON 
      ABS(c.latitude - w.latitude) < 0.00001 AND
      ABS(c.longitude - w.longitude) < 0.00001 AND
      ABS(c.altitude - w.altitude) < 0.1
    GROUP BY w.building_id
    ORDER BY maxWidth DESC
    """, nativeQuery = true)
    List<BuildingCrackWidthDto> getMaxCrackWidthPerBuilding();

    @Query("""
    SELECT c FROM Crack c
    WHERE c.latitude = :lat
      AND c.longitude = :lon
      AND c.altitude = :alt
    """)
    List<Crack> findByExactLocation(double lat, double lon, double alt);
}
