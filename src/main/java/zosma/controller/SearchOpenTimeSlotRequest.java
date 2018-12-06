package zosma.controller;

public class SearchOpenTimeSlotRequest {

	String scheduleID;
	int month;
	int year;
	int dayOfWeek;
	int dayOfMonth;
	
	public SearchOpenTimeSlotRequest(String scheduleID, int month, int year, int dayOfWeek, int dayOfMonth) {
		this.scheduleID = scheduleID;
		this.month = month;
		this.year = year;
		this.dayOfWeek = dayOfWeek;
		this.dayOfMonth = dayOfMonth;
	}
	
	public String toString() {
		return "ShowWeekSchedule(" + scheduleID + ","  + month + ","  + year + ","  + dayOfWeek + ","  + dayOfMonth + ")";
	}
}
