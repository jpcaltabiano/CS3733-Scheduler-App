package zosma.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

//Schedule Data Type
public class Schedule {
	private String name;
	String scheduleid;
	String code;
	ArrayList<Day> days = new ArrayList<>();
	LocalDateTime startDate;
	LocalDateTime endDate;
	int startHour;
	int endHour;
	int slotDuration;
	LocalDateTime createdDate;
	
	public Schedule() {
		this.name = "";
		this.startDate = null;
		this.endDate = null;
		this.startHour = -1;
		this.endHour = -1;
		this.slotDuration = -1;
		this.scheduleid = UUID.randomUUID().toString();
		this.code = new RandomString(8).nextString();
		this.createdDate = LocalDateTime.now();
	}
	
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
	
	public void setScheduleID(String id) {
		this.scheduleid = id;
	}
	
	public String getScheduleID() {
		return this.scheduleid;
	}
	
	public void setName(String nm) {
		this.name = nm;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setSDate(LocalDateTime sDate) {
		this.startDate = sDate;
	}
	
	public LocalDateTime getSDate() {
		return this.startDate;
	}
	
	public void setEDate(LocalDateTime eDate) {
		this.endDate = eDate;
	}
	
	public LocalDateTime getEDate() {
		return this.endDate;
	}
	
	public void setSHour(int sHour) {
		this.startHour = sHour;
	}
	
	public int getSHour() {
		return this.startHour;
	}
	
	public void setEHour(int eHour) {
		this.endHour = eHour;
	}
	
	public int getEHour() {
		return this.endHour;
	}
	
	public void setDur(int dur) {
		this.slotDuration = dur;
	}
	
	public int getDur() {
		return this.slotDuration;
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
			if(day.date.isAfter(startDate.toLocalDate()) || day.date.isBefore(endDate.toLocalDate())) {
				schedule.days.add(day);
			}
		}
		return schedule;
	}

	public Iterator<Day> days() {
		return this.days.iterator();
	}
	
}
