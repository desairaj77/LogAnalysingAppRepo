package com.desairaj.loggeranalysis.manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.desairaj.loggeranalysis.service.LoggerAnalysisService;

@Component
public class LoggerAnalysisManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(LoggerAnalysisManager.class);
	@Autowired
	private LoggerAnalysisService loggerAnalysisService;

	@Value("${pathToSourceFile}")
	private String pathToSourceFile;

	public void analyseLogs() {
		LOGGER.info("LoggerAnalysisManager::analyseLogs::START");

		try {
			File file = new File(pathToSourceFile + "logs.txt");
			if (!file.exists())
				throw new FileNotFoundException("File not found at location:{} " + pathToSourceFile);

			loggerAnalysisService.analyseLogs(file);
		} catch (IOException e) {
			LOGGER.error("!!! Unable to access the location: {}", pathToSourceFile);
		}

		LOGGER.info("LoggerAnalysisManager::analyseLogs::END");
	}

}
