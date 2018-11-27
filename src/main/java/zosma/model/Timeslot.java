package zosma.model;

public class Timeslot {

	String slotid;
	String date;
	int hour;
	boolean state;
	Meeting meeting;
	
	public Timeslot(String date, int hour) {
		this.date = date;
		this.hour = hour;
	}
	
}
