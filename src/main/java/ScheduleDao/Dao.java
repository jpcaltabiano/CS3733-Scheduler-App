package ScheduleDao;

import java.awt.List;
import java.util.Set;

import zosma.model.Schedule;

public interface Dao<T> {
	Schedule getSchedule(String id) throws Exception;
	boolean addSchedule(Schedule schedule) throws Exception;
	boolean updateSchedule(Schedule schedule) throws Exception;
	boolean removeSchedule(String id) throws Exception;
	Set<Schedule> getAllSchedules() throws Exception;
	List getSchedulesWithDate() throws Exception;
}
