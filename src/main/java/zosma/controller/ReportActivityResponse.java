package zosma.controller;

import java.util.Set;

import zosma.model.Schedule;

public class ReportActivityResponse {
	String message;
	Set<Schedule> schedules;
	int httpCode;

	public ReportActivityResponse (String message, Set<Schedule> schedules, int code) {
		this.message = message;
		this.schedules = schedules;
		this.httpCode = code;
	}

	public ReportActivityResponse (String errorMessage, int code) {
		this.message = errorMessage;
		this.schedules = null;
		this.httpCode = code;
	}

	public String toString() {
		if (schedules == null) { return message; }
		return "ShowWeekSchedule(" + message + "," + schedules.toString() + ")";
	}

}
