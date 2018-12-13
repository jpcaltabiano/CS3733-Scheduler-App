package zosma.model;

import java.time.LocalDate;
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
		this.meeting = new Meeting(this.slotid);
	}
	
	public Timeslot() {
		// TODO Auto-generated constructor stub
	}

	public boolean createMeeting(String user, String organizerCode) {
		if (this.meeting.user == null) {
			this.meeting = new Meeting(user,organizerCode, this.slotid);
			return true;
		}
		
		return false;
	}
	
	public boolean cancelMeeting(String code) {
		if(this.meeting.user != null && this.meeting.checkCode(code)) {
			this.meeting = new Meeting(this.slotid);
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

	public String getid() {
		return this.slotid;
	}

	public void setID(String id) {
		this.slotid = id;
		
	}

	public void setTM(LocalDateTime time) {
		this.time = time;
		
	}

	public void setST(boolean state) {
		this.state = state;
		
	}

	public void setME(Meeting meeting) {
		this.meeting = meeting;
		
	}

	public String getID() {
		return this.slotid;
	}

	public LocalDateTime getTM() {
		return this.time;
	}

	public boolean getST() {
		return this.state;
	}
	
}
