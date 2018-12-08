package zosma.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

//Schedule Data Type
public class Schedule {
	String name;
	String scheduleid;
	private String code;
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

	public boolean checkCode(String code) {
		return this.code.equals(code);
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

	public LocalDateTime getCreatedDate() {
		return this.createdDate;
	}

	public boolean createMeeting(String slotid, String user) {
		for (Day day : this.days) {
			if (day.createMeeting(slotid, user, this.code)) {
				return true;
			}
		}
		return false;
	}

	public boolean cancelMeeting(String slotid, String code) {
		Timeslot slot = getSlot(slotid);
		if(slot != null && slot.getMeeting() != null) {
			return slot.cancelMeeting(code);
		}
		return false;
	}

	public Timeslot getSlot(String slotid) {
		for (Day day : this.days) {
			if (day.hasSlot(slotid)) {
				return day.getSlot(slotid);
			}
		}
		return null;
	}

	public boolean extendStartDate(String start) {
		return true;
	}

	public boolean extendEndDate(String end) {
		return true;
	}

	/* TODO: Must be able to be (filtered by Month, Year, Day-Of-Week, Day-Of-Month, or Timeslot */
	public ArrayList<Timeslot> searchOpenSlots(int month, int year, int dayOfWeek, int dayOfMonth) {
		ArrayList<Timeslot> filterSlots = new ArrayList<Timeslot>();
		for (Day day : this.days) {
			for (Timeslot slot : day.slots) {
				if ((slot.time.getMonth().getValue() == month || month == 0) &&
						(slot.time.getYear() == year || year == 0) &&
						(slot.time.getDayOfWeek().getValue() == dayOfWeek || dayOfWeek == 0) &&
						(slot.time.getDayOfMonth() == dayOfMonth || dayOfMonth == 0)) {
					filterSlots.add(slot);
				}

			}
		}
		return filterSlots;
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
