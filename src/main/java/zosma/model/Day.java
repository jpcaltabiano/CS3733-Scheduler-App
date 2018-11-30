package zosma.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;

public class Day {
	
	LocalDate date;
	int startHour;
	int endHour;
	ArrayList<Timeslot> slots = new ArrayList<Timeslot>();
	
	public Day(LocalDateTime date, int startHour, int endHour, int duration) {
		this.date = date.toLocalDate();
		this.startHour = startHour;
		this.endHour = endHour;
		
		for (int i = 0; i < (this.endHour-this.startHour); i++ ) {
			for (int j = 0; j < (60/duration); j++) {
			LocalTime slotTime = date.withHour(startHour + i).withMinute(duration * j).toLocalTime();
			Timeslot ts = new Timeslot(slotTime.toString(),duration);
			slots.add(ts);
			}
		}
	}
	
	public boolean openSlot(Timeslot ts) {
		return true;
	}
	
	public boolean closeSlot(Timeslot ts) {
		return true;
	}
	
	public Iterator<Timeslot> slots() {
		return this.slots.iterator();
	}

}
