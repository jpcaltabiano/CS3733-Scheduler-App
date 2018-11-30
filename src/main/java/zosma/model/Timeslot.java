package zosma.model;

import java.util.UUID;

public class Timeslot {

	String slotid;
	String time;
	int slotDuration;
	boolean state;
	Meeting meeting;
	
	public Timeslot(String time, int duration) {
		this.slotid = UUID.randomUUID().toString();
		
		this.time = time;
		this.slotDuration = duration;
		this.state = true;
		this.meeting = null;
	}
	
}
