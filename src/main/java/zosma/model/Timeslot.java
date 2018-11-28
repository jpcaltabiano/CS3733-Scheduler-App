package zosma.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Timeslot {

	String slotid;
	LocalDateTime date;
	int slotDuration;
	boolean state;
	Meeting meeting;
	
	public Timeslot(LocalDateTime date, int duration) {
		this.slotid = UUID.randomUUID().toString();
		
		this.date = date;
		this.slotDuration = duration;
		this.state = true;
		this.meeting = null;
	}
	
}
