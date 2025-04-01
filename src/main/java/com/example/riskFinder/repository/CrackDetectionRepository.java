package com.example.riskFinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.riskFinder.model.CrackDetection;

public interface CrackDetectionRepository extends JpaRepository<CrackDetection, Long> {
}
