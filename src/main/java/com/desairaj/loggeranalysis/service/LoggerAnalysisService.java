package com.desairaj.loggeranalysis.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.desairaj.loggeranalysis.model.Event;
import com.desairaj.loggeranalysis.model.LogEventTracker;
import com.desairaj.loggeranalysis.repository.LoggerAnalysisRepository;
import com.desairaj.loggeranalysis.util.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

@Service
public class LoggerAnalysisService {
	private static final Logger LOGGER = LoggerFactory.getLogger(LoggerAnalysisService.class);
	@Autowired
	private LoggerAnalysisRepository repository;

	@Value("${maximumTimeLimit}")
	private Integer maximumTimeLimit;

	public void analyseLogs(File file) {
		LOGGER.info("LoggerAnalysisService::analyseLogs::START");
		Map<String, Event> eventMap = new HashMap<>();

		List<LogEventTracker> logs = new ArrayList<>();

		LOGGER.info("Parsing the events and persisting the alerts. This may take a while...");
		try (LineIterator lineIterator = FileUtils.lineIterator(file)) {
			int count = 0;
			while (lineIterator.hasNext()) {
				count++;
				try {
					Event event = new Gson().fromJson(lineIterator.nextLine(), Event.class);

					if (eventMap.containsKey(event.getId())) {
						Event existingEvent = eventMap.get(event.getId());
						Long eventDifference = setEventDifference(event, existingEvent);

						LogEventTracker logEventTracker = fillLogEventTracker(event, eventDifference);
						LOGGER.trace("Event with id {} processed", event.getId());
						logs.add(logEventTracker);
						eventMap.remove(event.getId());
					} else {
						eventMap.put(event.getId(), event);
					}
				} catch (JsonSyntaxException exception) {
					LOGGER.error("There was some issue with parsing the row:{}, {}", count, exception.getMessage());
				} catch (Exception exception) {
					LOGGER.error("There was some issue while processing the data: {}", exception.getMessage());
				}

			}
		} catch (IOException e) {
			LOGGER.error("Error while reading the file: {}", e.getMessage());
		}

		persistAlerts(logs);
		LOGGER.info("LoggerAnalysisService::analyseLogs::END");
	}

	private Long setEventDifference(Event event, Event existingEvent) {
		Long eventDifference = 0L;
		if (Constants.FINISHED.equals(existingEvent.getState())) {
			eventDifference = existingEvent.getTimestamp() - event.getTimestamp();
		} else {
			eventDifference = event.getTimestamp() - existingEvent.getTimestamp();
		}
		return eventDifference;
	}

	/**
	 * 
	 * @param logs
	 */
	private LogEventTracker fillLogEventTracker(Event event, Long eventDifference) {

		LogEventTracker logEventTracker = new LogEventTracker();
		logEventTracker.setId(event.getId());
		logEventTracker.setEventType(event.getType());
		logEventTracker.setHost(event.getHost());
		logEventTracker.setDuration(eventDifference);

		if (eventDifference > maximumTimeLimit) {
			logEventTracker.setAlert(Boolean.TRUE);
		}
		return logEventTracker;
	}

	/**
	 * 
	 * @param logs
	 */

	private void persistAlerts(List<LogEventTracker> logs) {
		LOGGER.debug("Saving all logs {}", logs.size());
		repository.saveAll(logs);
		LOGGER.debug("All records saved");
	}
}
