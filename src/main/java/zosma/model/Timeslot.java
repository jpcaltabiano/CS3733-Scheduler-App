package zosma.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Timeslot {

	String slotid;
	LocalDateTime time;
	boolean state;
	Meeting meeting;
	
	public Timeslot(LocalDateTime time) {
		this.slotid = UUID.randomUUID().toString();
		
		this.time = time;
		this.state = true;
		this.meeting = null;
	}
	
	public boolean createMeeting(String user, String organizerCode, String participantCode) {
		if (this.meeting == null) {
			this.meeting = new Meeting(user,organizerCode, participantCode);
			return true;
		}
		
		return false;
	}
	
	public boolean cancelMeeting(String code) {
		if(this.meeting != null && this.meeting.checkCode(code)) {
			this.meeting = null;
			return true;
		}
		return false;
	}
	
	public Meeting getMeeting() {
		return this.meeting;
	}
	
	public LocalDateTime getTime() {
		return this.time;
	}
	
}
