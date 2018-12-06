package zosma.controller;

import zosma.model.Meeting;

public class CreateMeetingResponse {
	String message;
	Meeting meeting;
	int httpCode;
	
	public CreateMeetingResponse (String message, Meeting meeting, int code) {
		this.message = message;
		this.meeting = meeting;
		this.httpCode = code;
	}
	
	public CreateMeetingResponse (String errorMessage, int code) {
		this.message = errorMessage;
		this.meeting = null;
		this.httpCode = code;
	}
	
	public String toString() {
		if (meeting == null) { return message; }
		return "CreateMeeting(" + message + "," + meeting.toString() + ")";
	}
}
