package zosma.model;

import java.util.ArrayList;
import java.util.Iterator;

public class Schedule {
	
	String scheduleid;
	String code;
	ArrayList<Day> day = new ArrayList<>();
	String startDate;
	String endDate;
	int startHour;
	int endHour;
	int slotDuration;
	String createdDate;
	
	public Schedule(String startDate, String endDate, int startHour, int endHour, int duration) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.startHour = startHour;
		this.endHour = endHour;
		this.slotDuration = duration;
	}
	
	public boolean createMeeting(Meeting m, String day, Timeslot slot) {
		return true;
	}
	
	public boolean cancelMeeting(String slotid) {
		return true;
	}
	
	public boolean extendStartDate(String start) {
		return true;
	}
	
	public boolean extendEndDate(String end) {
		return true;
	}
	
	/* TODO: Must be able to be (filtered by Month, Year, Day-Of-Week, Day-Of-Month, or Timeslot */
	public ArrayList<Timeslot> searchOpenSlots() {
		return null;
	}
	
	public boolean showWeekSchedule(Schedule s) {
		return true;
	}
	
	public Iterator<Day> days() {
		return this.day.iterator();
	}

}
