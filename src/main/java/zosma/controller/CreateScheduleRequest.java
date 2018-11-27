package zosma.controller;

public class CreateScheduleRequest {
	String startDate;
	String endDate;
	int startHour;
	int endHour;
	int slotDuration;
	
	public CreateScheduleRequest(String startDate, String endDate, int startHour, int endHour, int duration) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.startHour = startHour;
		this.endHour = endHour;
		this.slotDuration = duration;
	}
	
	public String toString() {
		return "CreateSchedule(" + startDate + "," + endDate + "," + 
					startHour + "," + endHour + "," + slotDuration +")";
	}
}
