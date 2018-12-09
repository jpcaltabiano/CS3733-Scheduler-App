package zosma.controller;

import zosma.model.Meeting;

public class CreateMeetingResponse {
	String message;
	Meeting meeting;
	String participantCode;
	int httpCode;
	
	public CreateMeetingResponse (String message, Meeting meeting,String participantCode, int code) {
		this.message = message;
		this.meeting = meeting;
		this.participantCode = participantCode;
		this.httpCode = code;
	}
	
	public CreateMeetingResponse (String errorMessage, int code) {
		this.message = errorMessage;
		this.meeting = null;
		this.participantCode = null;
		this.httpCode = code;
	}
	
	public String toString() {
		if (meeting == null) { return message; }
		return "CreateMeeting(" + message + "," + meeting.toString() + "," + participantCode + ")";
	}
}
