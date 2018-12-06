package zosma.controller;

import zosma.model.Meeting;

public class CancelMeetingResponse {
	String message;
	int httpCode;
	
	public CancelMeetingResponse (String message, int code) {
		this.message = message;
		this.httpCode = code;
	}
	
	public String toString() {
		return "CreateMeeting(" + message + ")";
	}
}
