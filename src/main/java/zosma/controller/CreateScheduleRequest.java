package zosma.controller;

public class CreateScheduleRequest {
	String name;
	String startDate;
	String endDate;
	int startHour;
	int endHour;
	int slotDuration;
	
	public CreateScheduleRequest(String name, String startDate, String endDate, int startHour, int endHour, int duration) {
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.startHour = startHour;
		this.endHour = endHour;
		this.slotDuration = duration;
	}
	
	public String toString() {
		return "CreateSchedule(" + startDate.toString() + "," + endDate.toString() + "," + 
					startHour + "," + endHour + "," + slotDuration +")";
	}
}
