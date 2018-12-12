package zosma.controller;

public class CancelMeetingRequest {
	String scheduleID;
	String slotID;
	String code;
	
	public CancelMeetingRequest(String scheduleID, String slotID, String code) {
		this.scheduleID = scheduleID;
		this.slotID = slotID;
		this.code = code;
	}
	
	public String toString() {
		return "CancelMeeting(" + scheduleID + ","  + slotID + ","  + code + ")";
	}
}
