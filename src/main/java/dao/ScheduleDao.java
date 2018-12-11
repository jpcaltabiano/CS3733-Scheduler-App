package dao;

import java.awt.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.json.simple.parser.ConnectionFactory;

import zosma.model.Day;
import zosma.model.Schedule;


public class ScheduleDao {
    
    Connection conn;
    
    public ScheduleDao(){
        try {
            conn = ConnectionFactory.getConnection();
        }catch (Exception e){
            conn = null;
        }
    }
    
    public Schedule getSchedule(String id) throws Exception {
        
            try {
                
                Schedule schedule = null;
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM schedule WHERE idschedule=?;");
                stmt.setString(1, id);
                ResultSet rs = stmt.executeQuery();
                
                while(rs.next()){
                    schedule = extractScheduleFromRes(rs);
                }
                rs.close();
                stmt.close();
                
                return schedule;
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception("getSchedule failed finding schedule: " + e.getMessage());
            }
    }
    private Schedule extractScheduleFromRes(ResultSet rs) throws Exception{
        Schedule schedule = new Schedule();
        schedule.setScheduleID(rs.getString("idschedule"));
        schedule.setName(rs.getString("name"));
        schedule.setSDate(LocalDateTime.parse(rs.getString("sDate")));
        schedule.setEDate(LocalDateTime.parse(rs.getString("eDate")));
        schedule.setSHour(rs.getInt("sHour"));
        schedule.setEHour(rs.getInt("eHour"));
        schedule.setDur(rs.getInt("dur"));
        schedule.setCode(rs.getString("code"));
        
        DayDao daydao = new DayDao();
        ArrayList<Day> days = daydao.getDayBySchedule(schedule.getScheduleID());
        schedule.setDays(days);
        return schedule;
        
    }
    public ArrayList<Schedule> getAllSchedules() throws Exception {
        
        ArrayList<Schedule> schedules = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM schedule");
            while(rs.next())
            {
                Schedule schedule = extractScheduleFromRes(rs);
                schedules.add(schedule);
            }
            rs.close();
            stmt.close();
            return schedules;
        } catch (Exception e) {
            throw new Exception("getAllSchedules failed finding all Schedules: " + e.getMessage());
        }
    }
    public Schedule getSchedulesBySC(String secretCode) throws Exception{
        try {
            Schedule schedule = null;
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM schedule WHERE idschedule=?;");
            ps.setString(7,  secretCode);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                schedule = extractScheduleFromRes(rs);
            }
            rs.close();
            ps.close();
            return schedule;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("getSchedulesBySC failed finding schedule by secret code: " + e.getMessage());
        }
    }
    public boolean addSchedule(Schedule schedule) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO schedule VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, schedule.getScheduleID());
            ps.setString(2, schedule.getName());
            ps.setString(3, schedule.getSDate().toString());
            ps.setString(4, schedule.getEDate().toString());
            ps.setInt(5, schedule.getSHour());
            ps.setInt(6, schedule.getEHour());
            ps.setInt(7, schedule.getDur());
            ps.setString(8, schedule.getCode());
            
            DayDao daydao = new DayDao();
            for (Day day : schedule.getDays()) {
            	daydao.addDay(schedule.getScheduleID(),day);
            }
            
            int i = ps.executeUpdate();
            if(i == 1) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean updateSchedule(Schedule schedule) throws Exception {
        Connection connection = ConnectionFactory.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE schedule SET name=?, sDate=?, eDate=?, sHour=?, eHour=?, dur=?, code=? WHERE idschedule=?");
            ps.setString(1, schedule.getName());
            ps.setString(2, schedule.getSDate().toString());
            ps.setString(3, schedule.getEDate().toString());
            ps.setInt(4, schedule.getSHour());
            ps.setInt(5, schedule.getEHour());
            ps.setInt(6, schedule.getDur());
            ps.setString(7, schedule.getCode());
            ps.setString(8,schedule.getScheduleID());
            
            DayDao daydao = new DayDao();
            for (Day day : schedule.getDays()) {
            	daydao.updateDay(schedule.getScheduleID(),day);
            }
            
            int i = ps.executeUpdate();
          if(i == 1) {
        return true;
          }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    public boolean removeSchedule(String id) throws Exception {
        try {
            Statement stmt = conn.createStatement();
            int i = stmt.executeUpdate("DELETE FROM schedule WHERE idschedule=?");
          if(i == 1) {
              return true;
          }
        } catch (Exception e) {
            throw new Exception("Failed to delete schedule: " + e.getMessage());
            
        }
        return false;
    }
}

