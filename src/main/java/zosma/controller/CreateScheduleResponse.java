package zosma.controller;

import zosma.model.Schedule;

public class CreateScheduleResponse {
	String message;
	Schedule schedule;
	String scheduleID;
	String secretCode;
	int httpCode;
	
	public CreateScheduleResponse (String message, Schedule schedule, String secretCode, int code) {
		this.message = message;
		this.schedule = schedule;
		this.scheduleID = schedule.getScheduleID();
		this.secretCode = secretCode;
		this.httpCode = code;
	}
	
	public CreateScheduleResponse (String errorMessage, int code) {
		this.message = errorMessage;
		this.schedule = null;
		this.secretCode = null;
		this.httpCode = code;
	}
	
	public String toString() {
		if (schedule == null) { return message; }
		return "CreateSchedule(" + message + "," + schedule.toString() + "," + secretCode  + ")";
	}
}
