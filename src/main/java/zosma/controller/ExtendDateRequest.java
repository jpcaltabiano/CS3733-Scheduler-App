package zosma.controller;

public class ExtendDateRequest {
	String scheduleID;
	String startDate;
	String endDate;
	String code;
	
	public ExtendDateRequest(String scheduleID, String startDate, String endDate, String code) {
		this.scheduleID = scheduleID;
		this.startDate = startDate;
		this.endDate = endDate;
		this.code = code;
	}
	
	public String toString() {
		return "OpenTimeSlot(" + scheduleID + ","  + startDate + ","  + endDate + ","  + code + ")";
	}
}
