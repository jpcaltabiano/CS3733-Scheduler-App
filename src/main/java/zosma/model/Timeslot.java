package zosma.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Timeslot {

	String slotid;
	LocalDateTime time;
	boolean state;
	Meeting meeting;
	
	public Timeslot(LocalDateTime time) {
		this.slotid = new RandomString(8).nextString();
		
		this.time = time;
		this.state = true;
		this.meeting = null;
	}
	
	public boolean createMeeting(String user, String organizerCode) {
		if (this.meeting == null) {
			this.meeting = new Meeting(user,organizerCode, this.slotid);
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
