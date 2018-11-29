package zosma.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

public class Schedule {

	String scheduleid;
	String name;
	String code;
	ArrayList<Day> days = new ArrayList<>();
	LocalDateTime startDate;
	LocalDateTime endDate;
	int startHour;
	int endHour;
	int slotDuration;
	LocalDateTime createdDate;

	public Schedule(String name, LocalDateTime startDate, LocalDateTime endDate, int startHour, int endHour, int duration) {
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.startHour = startHour;
		this.endHour = endHour;
		this.slotDuration = duration;

		this.scheduleid = UUID.randomUUID().toString();
		this.code = new RandomString(8).nextString();
		this.createdDate = LocalDateTime.now();

		for (int i = 0; i < (this.endDate.getDayOfYear()-this.startDate.getDayOfYear()); i++ ) {
			LocalDateTime dDate = startDate.plusDays(i);

			//monday-friday is 1-5
			if(dDate.getDayOfWeek().getValue() < 6) {
				Day day = new Day(dDate,this.startHour,this.endHour,this.slotDuration);
				days.add(day);
			}
		}
	}

	public String getScheduleID() {
		return this.scheduleid;
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

	public Schedule showWeekSchedule(LocalDateTime startDate, LocalDateTime endDate) {
		Schedule schedule = new Schedule(this.name + ": Week Schedule",startDate,endDate,this.startHour,this.endHour,this.slotDuration);
		schedule.days.clear();
		
		for (Day day: this.days) {
			if(day.date.isAfter(startDate.toLocalDate()) && day.date.isBefore(endDate.toLocalDate())) {
				schedule.days.add(day);
			}
		}
		return schedule;
	}

	public Iterator<Day> days() {
		return this.days.iterator();
	}

}
