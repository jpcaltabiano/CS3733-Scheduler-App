package zosma.model;

import java.time.LocalTime;
import java.util.UUID;

public class Timeslot {

	String slotid;
	LocalTime date;
	int slotDuration;
	boolean state;
	Meeting meeting;
	
	public Timeslot(LocalTime date, int duration) {
		this.slotid = UUID.randomUUID().toString();
		
		this.date = date;
		this.slotDuration = duration;
		this.state = true;
		this.meeting = null;
	}
	
}
