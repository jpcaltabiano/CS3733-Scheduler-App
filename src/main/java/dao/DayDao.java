package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.json.simple.parser.ConnectionFactory;

import zosma.model.Day;
import zosma.model.Schedule;

public class DayDao {
    
    Connection conn;
    
    public DayDao() {
        try {
            conn = ConnectionFactory.getConnection();
        }catch (Exception e){
            conn = null;
        }
    }
    
    private Day genDay(ResultSet rs) throws Exception{
        String id = rs.getString("dayID");
        LocalDateTime date = LocalDateTime.parse(rs.getString("date"));
        int sHour = rs.getInt("sHour");
        int eHour = rs.getInt("eHour");
        int dur = rs.getInt("duration");
        Day d = new Day(date, sHour, eHour, dur);
        d.setCode(id);
        return d;
    }
    
    public Day getDay(String id) throws Exception{
        try {
            Day day = null;
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM day WHERE dayID=?;");
            ps.setString(1, id);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
            	if(resultSet.getString("scheduleID").equals(id)) {
                day = genDay(resultSet);
            	}
            }
            resultSet.close();
            ps.close();

            return day;

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("getDay failed finding day: " + e.getMessage());
        }
    }
public ArrayList<Day> getDayBySchedule(String id) throws Exception {
        
        ArrayList<Day> days = new ArrayList<>();
        try {
        	PreparedStatement ps =  conn.prepareStatement("SELECT * FROM schedule WHERE scheduleID=?;");
        	ps.setString(1,  id);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
            	Day day = genDay(rs);
                days.add(day);
            }
            rs.close();
            ps.close();
            return days;
        } catch (Exception e) {
            throw new Exception("getDayBySchedule failed finding days: " + e.getMessage());
        }
    }
    
    public boolean deleteDay(Day day) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM day WHERE date=?;");
            ps.setString(1, day.getDate().toString());
            int numAffected = ps.executeUpdate();
            ps.close();

            return (numAffected == 1);

        } catch (Exception e) {
            throw new Exception("deleteDay failed to delete meeting: " + e.getMessage());
        }
    }
    
    public boolean updateDay(String scheduleid, Day day) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("UPDATE day SET dayID=?, sHour=?, eHour=? WHERE dateID=?;");
            ps.setString(1, day.getCode());
            ps.setString(2, day.getDate().toString());
            ps.setInt(3, day.getSH());
            ps.setInt(4, day.getEH());
            
            int numAffected = ps.executeUpdate();
            ps.close();
    
            return (numAffected == 1);
        } catch (Exception e) {
            throw new Exception("updateDay failed to update day: " + e.getMessage());
            }
        }
    
    public boolean addDay(String scheduleid, Day day) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO day VALUES (?, ?, ?, ?, ?)");
            ps.setString(1, day.getCode());
            ps.setString(2, scheduleid);
            ps.setString(3, day.getDate().toString());
            ps.setInt(4, day.getSH());
            ps.setInt(5, day.getEH());
            ps.execute();
            return true;
        }catch (Exception e){
            throw new Exception("addDay failed to add day: " + e.getMessage());
        }
    }
}