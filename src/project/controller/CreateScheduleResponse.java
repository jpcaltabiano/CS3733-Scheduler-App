package project.controller;

import project.model.Schedule;

public class CreateScheduleResponse {
	Schedule schedule;
	int httpCode;
	
	public CreateScheduleResponse (Schedule schedule, int code) {
		this.schedule = schedule;
		this.httpCode = code;
	}
	
	public CreateScheduleResponse (int code) {
		this.schedule = null;
		this.httpCode = code;
	}
	
	public String toString() {
		if (schedule == null) { return "Fail to create schedule"; }
		return "CreateSchedule(" + schedule.toString() + ")";
	}
}
