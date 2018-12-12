package zosma.controller;

public class DeleteScheduleRequest {
	String scheduleID;
	String code;
	
	public DeleteScheduleRequest(String scheduleID, String code) {
		this.scheduleID = scheduleID;
		this.code = code;
	}
	
	public String toString() {
		return "DeleteSchedule(" + scheduleID + ","  + code + ")";
	}
}
