package zosma.controller;

public class DeleteScheduleResponse {
	String message;
	int httpCode;
	
	public DeleteScheduleResponse (String message, int code) {
		this.message = message;
		this.httpCode = code;
	}
	
	public String toString() {
		return "DeleteSchedule(" + message + ")";
	}
}
