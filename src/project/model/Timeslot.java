package project.model;

import java.util.Date;

public class Timeslot {

	String slotid;
	Date date;
	int hour;
	boolean state;
	Meeting meeting;
	
	public Timeslot(Date date, int hour) {
		this.date = date;
		this.hour = hour;
	}
	
}
