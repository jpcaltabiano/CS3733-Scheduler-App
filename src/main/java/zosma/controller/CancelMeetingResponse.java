package zosma.controller;

public class CancelMeetingResponse {
	String message;
	int httpCode;
	
	public CancelMeetingResponse (String message, int code) {
		this.message = message;
		this.httpCode = code;
	}
	
	public String toString() {
		return "CancelMeeting(" + message + ")";
	}
}
