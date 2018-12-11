package dao;

import java.awt.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.json.simple.parser.ConnectionFactory;

import zosma.model.Meeting;

public class MeetingDao {
    
    Connection conn;
    
    public MeetingDao() {
        try {
            conn = ConnectionFactory.getConnection();
        }catch (Exception e){
            conn = null;
        }
    }
    
    private Meeting genMeeting(ResultSet rs) throws Exception{
        String psc = rs.getString("participantSC");
        String osc = rs.getString("organizerSC");
        String u = rs.getString("user");
        String sid = rs.getString("slotID");
        Meeting m = new Meeting(u, osc, sid);
        m.setPSC(psc);
        return m;
    }
    
    public Meeting getMeeting(String psc) throws Exception{
        try {
            Meeting meeting = null;
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM meeting WHERE slotID=?;");
            ps.setString(3, psc);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                meeting = genMeeting(resultSet);
            }
            resultSet.close();
            ps.close();

            return meeting;

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("getMeeting failed finding meeting: " + e.getMessage());
        }
    }
    
    public boolean deleteMeeting(Meeting meeting) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM meeting WHERE slotID=?;");
            ps.setString(3, meeting.getPSC());
            int numAffected = ps.executeUpdate();
            ps.close();

            return (numAffected == 1);

        } catch (Exception e) {
            throw new Exception("deleteMeeting failed to delete meeting: " + e.getMessage());
        }
    }
    
    public boolean updateMeeting(Meeting meeting) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("UPDATE meeting SET user=?, organizerSC=?, participantSC=? WHERE slotID=?;");
            
            ps.setString(1, meeting.getUser());
            ps.setString(2, meeting.getOSC());
            ps.setString(3, meeting.getPSC());
            ps.setString(4, meeting.getSID());
            
            int numAffected = ps.executeUpdate();
            ps.close();
    
            return (numAffected == 1);
        } catch (Exception e) {
            throw new Exception("Failed to update meeting: " + e.getMessage());
            }
        }
    
    public boolean addMeeting(Meeting meeting) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO meeting VALUES (?, ?, ?, ?)");
            ps.setString(1, meeting.getUser());
            ps.setString(2, meeting.getOSC());
            ps.setString(3, meeting.getPSC());
            ps.setString(4, meeting.getSID());
            ps.execute();
            return true;
        }catch (Exception e){
            throw new Exception("addMeeting failed to add meeting: " + e.getMessage());
        }
    }

}