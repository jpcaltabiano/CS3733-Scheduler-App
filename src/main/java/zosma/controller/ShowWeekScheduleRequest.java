package zosma.controller;

public class ShowWeekScheduleRequest {
	
	String scheduleID;
	String startDate;
	String endDate;
	
	public ShowWeekScheduleRequest(String scheduleID, String startDate, String endDate) {
		this.scheduleID = scheduleID;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public String toString() {
		return "ShowWeekSchedule(" + scheduleID + ","  + startDate + "," + endDate + ")";
	}
}
