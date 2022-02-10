package com.desairaj.loggeranalysis.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.desairaj.loggeranalysis.model.LogEventTracker;

public interface LoggerAnalysisRepository extends JpaRepository<LogEventTracker, Integer> {

}
