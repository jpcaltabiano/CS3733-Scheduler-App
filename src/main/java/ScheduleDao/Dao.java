package ScheduleDao;

import java.awt.List;
import java.util.Set;

import zosma.model.Schedule;

public interface Dao<T> {
	Schedule getSchedule(String id);
	boolean addSchedule(Schedule schedule);
	boolean updateSchedule(Schedule schedule);
	boolean removeSchedule(String id);
	Set<Schedule> getAllSchedules();
	List getSchedulesWithDate();
}
