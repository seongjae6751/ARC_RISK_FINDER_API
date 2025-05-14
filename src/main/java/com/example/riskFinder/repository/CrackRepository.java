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

    @Query("""
    SELECT w.building.id AS buildingId, COUNT(c.id) AS crackCount
    FROM Crack c
    JOIN Waypoint w ON c.crackId = w.crackId
    GROUP BY w.building.id
    ORDER BY crackCount DESC
""")
    List<BuildingCrackCountDto> getCrackCountPerBuilding();

    @Query("""
    SELECT w.building.id AS buildingId, MAX(cm.widthMm) AS maxWidth
    FROM CrackMeasurement cm
    JOIN Waypoint w ON cm.crackId = w.crackId
    GROUP BY w.building.id
    ORDER BY maxWidth DESC
""")
    List<BuildingCrackWidthDto> getMaxCrackWidthPerBuilding();
}
