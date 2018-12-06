package zosma.controller;

public class CreateMeetingRequest {
	String scheduleID;
	String slotID;
	String user;
	
	public CreateMeetingRequest(String scheduleID, String slotID, String user) {
		this.scheduleID = scheduleID;
		this.slotID = slotID;
		this.user = user;
	}
	
	public String toString() {
		return "CreateMeeting(" + scheduleID + ","  + slotID + ","  + user + ")";
	}
}
