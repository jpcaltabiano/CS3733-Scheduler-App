package zosma.model;

import java.time.LocalTime;
import java.util.UUID;

public class Timeslot {

	String slotid;
	LocalTime time;
	int slotDuration;
	boolean state;
	Meeting meeting;
	
	public Timeslot(LocalTime time, int duration) {
		this.slotid = UUID.randomUUID().toString();
		
		this.time = time;
		this.slotDuration = duration;
		this.state = true;
		this.meeting = null;
	}
	
}
