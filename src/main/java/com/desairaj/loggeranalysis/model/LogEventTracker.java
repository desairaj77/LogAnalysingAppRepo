package com.desairaj.loggeranalysis.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "LOG_EVENT_TRACKER")
public class LogEventTracker {

	@Id
	private String id;
	@Column
	private float duration;
	@Column
	private String eventType;
	@Column
	private String host;
	@Column
	private boolean alert;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public float getDuration() {
		return duration;
	}

	public void setDuration(float duration) {
		this.duration = duration;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public boolean isAlert() {
		return alert;
	}

	public void setAlert(boolean alert) {
		this.alert = alert;
	}

}
