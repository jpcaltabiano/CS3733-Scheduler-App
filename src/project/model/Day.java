package project.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Date;

public class Day {
	
	int start;
	int end;
	Date date;
	ArrayList<Timeslot> slots = new ArrayList<>();
	
	public Day(int start, int end) {
		this.start = start;
		this.end = end;
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
