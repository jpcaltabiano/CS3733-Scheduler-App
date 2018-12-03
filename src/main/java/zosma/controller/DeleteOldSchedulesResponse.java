package zosma.controller;

public class DeleteOldSchedulesResponse {
	String message;
	int httpCode;

	public DeleteOldSchedulesResponse (String message, int code) {
		this.message = message;
		this.httpCode = code;
	}

	public String toString() {
		return "ShowWeekSchedule(" + message + ")";
	}
}
