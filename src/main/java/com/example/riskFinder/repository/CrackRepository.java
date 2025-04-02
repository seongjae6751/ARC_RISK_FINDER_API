package com.example.riskFinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.riskFinder.model.Crack;

import java.time.LocalDate;

public interface CrackRepository extends JpaRepository<Crack, Long> {

    LocalDate findLatestDetectionDate(String crackId);
}
