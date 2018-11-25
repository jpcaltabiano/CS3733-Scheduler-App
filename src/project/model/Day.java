package project.model;

import java.util.ArrayList;
import java.util.Iterator;

public class Day {
	
	int start;
	int end;
	String date;
	ArrayList<Timeslot> slots = new ArrayList<>();
	
	public Day(int start, int end, String date) {
		this.start = start;
		this.end = end;
		this.date = date;
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
