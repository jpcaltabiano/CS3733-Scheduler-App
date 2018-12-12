package zosma.controller;

public class OpenTimeSlotRequest {
	String scheduleID;
	String slotID;
	String date;
	String time;
	String code;
	
	public OpenTimeSlotRequest(String scheduleID, String slotID, String date, String time,String code) {
		this.scheduleID = scheduleID;
		this.slotID = slotID;
		this.date = date;
		this.time = time;
		this.code = code;
	}
	
	public String toString() {
		return "OpenTimeSlot(" + scheduleID + ","  + slotID + "," + date + "," + time + ","  + code + ")";
	}
}
