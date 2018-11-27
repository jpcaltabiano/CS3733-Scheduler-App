package zosma.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class System {
	
	ArrayList<Schedule> schedules;

	public System(ArrayList<Schedule> schedules) {}
	
	public boolean createSchedule(Schedule s) {
		return true;
	}
	
	public boolean deleteSchedule(String id) {
		return true;
	}
	
	public boolean deleteOldSchedules(Date day) {
		return true;
	}
	
	public ArrayList<Schedule> reportActivity(int hour) {
		return null;
	}
	
	public Iterator<Schedule> schedules() {
		return this.schedules.iterator();
	}

}
