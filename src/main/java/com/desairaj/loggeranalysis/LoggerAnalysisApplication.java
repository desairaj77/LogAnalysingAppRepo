package com.desairaj.loggeranalysis;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.desairaj.loggeranalysis.manager.LoggerAnalysisManager;

@SpringBootApplication
public class LoggerAnalysisApplication {
	private static final Logger LOGGER = LoggerFactory.getLogger(LoggerAnalysisApplication.class);

	@Autowired
	private LoggerAnalysisManager loggerAnalysisManager;

	public static void main(String[] args) {
		SpringApplication.run(LoggerAnalysisApplication.class, args);
	}

	@PostConstruct
	public void analyseLogs() {
		LOGGER.info("LoggerAnalysisApplication::run::START");
		loggerAnalysisManager.analyseLogs();
		LOGGER.info("LoggerAnalysisApplication::run::END");
	}

}
