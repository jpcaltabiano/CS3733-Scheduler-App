package zosma.controller;

import zosma.model.Schedule;

public class ExtendDateResponse {
	String message;
	Schedule schedule;
	int httpCode;
	
	public ExtendDateResponse (String message, Schedule schedule, int code) {
		this.message = message;
		this.schedule = schedule;
		this.httpCode = code;
	}
	
	public ExtendDateResponse (String errorMessage, int code) {
		this.message = errorMessage;
		this.schedule = null;
		this.httpCode = code;
	}
	
	public String toString() {
		if (schedule == null) { return message; }
		return "ExtendDate(" + message + "," + schedule.toString() + ")";
	}
}
