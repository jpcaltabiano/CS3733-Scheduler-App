package zosma.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

public class Day {

	String dayid;
	LocalDate date;
	int startHour;
	int endHour;
	ArrayList<Timeslot> slots = new ArrayList<Timeslot>();

	public Day(LocalDateTime date, int startHour, int endHour, int duration) {
		this.date = date.toLocalDate();
		this.startHour = startHour;
		this.endHour = endHour;
		
		this.dayid = new RandomString(8).nextString();

		for (int i = 0; i < (this.endHour-this.startHour); i++ ) {
			for (int j = 0; j < (60/duration); j++) {
				LocalDateTime slotTime = date.withHour(startHour + i).withMinute(duration * j);
				Timeslot ts = new Timeslot(slotTime);
				slots.add(ts);
			}
		}
	}

	public Day() {
		// TODO Auto-generated constructor stub
	}

	public boolean createMeeting(String slotid, String user,String organizerCode) {
		for (Timeslot slot : this.slots) {
			if (slot.slotid.equals(slotid)) {
				return slot.createMeeting(user,organizerCode);
			}
		}
		return false;
	}

	public boolean openSlot(Timeslot ts) {
		return true;
	}

	public boolean closeSlot(Timeslot ts) {
		return true;
	}

	public boolean hasSlot(String slotid) {
		for (Timeslot slot : slots) {
			if (slot.slotid.equals(slotid)) {
				return true;
			}
		}
		return false;
	}

	public Timeslot getSlot(String slotid) {
		for (Timeslot slot : slots) {
			if (slot.slotid.equals(slotid)) {
				return slot;
			}
		}
		return null;
	}

	public Iterator<Timeslot> slots() {
		return this.slots.iterator();
	}

	public void setCode(String id) {
		this.dayid = id;
		
	}

	public Object getDate() {
		return this.date;
	}

	public String getCode() {
		return this.dayid;
	}

	public int getSH() {
		return this.startHour;
	}

	public int getEH() {
		return this.endHour;
	}

	public void setSH(int sHour) {
		this.startHour = sHour;
		
	}

	public void setEH(int eHour) {
		this.endHour = eHour;
		
	}

	public void setdate(LocalDate date) {
		this.date = date;
		
	}

	public void setSlot(ArrayList<Timeslot> slots) {
		this.slots = slots;		
	}

	public ArrayList<Timeslot> getSlot() {
		return this.slots;
	}

}
