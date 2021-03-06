package zosma.controller;

public class CloseTimeSlotRequest {
	String scheduleID;
	String slotID;
	String date;
	String time;
	String code;
	
	public CloseTimeSlotRequest(String scheduleID, String slotID, String date, String time,String code) {
		this.scheduleID = scheduleID;
		this.slotID = slotID;
		this.date = date;
		this.time = time;
		this.code = code;
	}
	
	public String toString() {
		return "CloseTimeSlot(" + scheduleID + ","  + slotID + "," + date + "," + time + ","  + code + ")";
	}
}
