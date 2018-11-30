package ScheduleDao;

import java.awt.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.json.simple.parser.ConnectionFactory;

import zosma.model.Schedule;


public class ScheduleDao  implements Dao<Schedule>  {
	
private ArrayList<Schedule> schedules = new ArrayList<>();
	
	@Override
	public Schedule getSchedule(int id) {
		 Connection connection = ConnectionFactory.getConnection();
	        try {
	            Statement stmt = connection.createStatement();
	            ResultSet rs = stmt.executeQuery("SELECT * FROM user WHERE id=" + id);
	            if(rs.next())
	            {
	            	return extractScheduleFromRes(rs);
	            }
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
	    return null;
	}
	
	private Schedule extractScheduleFromRes(ResultSet rs) throws SQLException{
		Schedule schedule = new Schedule();
		schedule.setScheduleID(rs.getString("id"));
		schedule.setName(rs.getString("nm"));
		schedule.setSDate(rs.getString("sDate"));
		schedule.setEDate(rs.getString("eDate"));
		schedule.setSHour(rs.getString("sHour"));
		schedule.setEHour(rs.getString("eHour"));
		schedule.setDur(rs.getInt("dur"));
		
		return schedule;
		
	}

	@Override
	public Set<Schedule> getAllSchedules() {
		Connector connector = new Connector();
	    Connection connection = connector.getConnection();
	    try {
	        Statement stmt = connection.createStatement();
	        ResultSet rs = stmt.executeQuery("SELECT * FROM schedule");
	        Set schedules = new HashSet();
	        while(rs.next())
	        {
	            Schedule schedule = extractScheduleFromRes(rs);
	            schedules.add(schedule);
	        }
	        return schedules;
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	    return null;
	}

	@Override
	public List getSchedulesWithDate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addSchedule(Schedule schedule) {
		Connector connector = new Connector();
	    Connection connection = connector.getConnection();
	    try {
	        PreparedStatement ps = connection.prepareStatement("INSERT INTO schedule VALUES (?, ?, ?, ?, ?, ?)");
	        ps.setString(1, schedule.getName());
	        ps.setString(2, schedule.getSDate());
	        ps.setString(3, schedule.getEDate());
	        ps.setString(4, schedule.getSHour());
	        ps.setString(5, schedule.getEHour());
	        ps.setInt(6, schedule.getDur());
	        
	        int i = ps.executeUpdate();
	        if(i == 1) {
	        	return true;
	        }
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	    return false;
	}

	@Override
	public boolean updateSchedule(Schedule schedule) {
		Connector connector = new Connector();
	    Connection connection = connector.getConnection();
	    try {
	        PreparedStatement ps = connection.prepareStatement("UPDATE schedule SET name=?, sDate=?, eDate=?, sHour=?, eHour=?, dur=? WHERE id=?");
	        ps.setString(1, schedule.getName());
	        ps.setLong(2, schedule.getSDate());
	        ps.setString(3, schedule.getEDate());
	        ps.setString(4, schedule.getSHour());
	        ps.setString(5, schedule.getEHour());
	        ps.setInt(6, schedule.getDur());
	        int i = ps.executeUpdate();
	      if(i == 1) {
	    return true;
	      }
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	    return false;
	}

	@Override
	public boolean removeSchedule(int id) {
		Connector connector = new Connector();
	    Connection connection = connector.getConnection();
	    try {
	        Statement stmt = connection.createStatement();
	        int i = stmt.executeUpdate("DELETE FROM schedule WHERE id=" + id);
	      if(i == 1) {
	    return true;
	      }
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	    return false;
	}
}
