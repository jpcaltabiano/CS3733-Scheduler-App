package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.json.simple.parser.ConnectionFactory;

import zosma.model.Day;
import zosma.model.Meeting;
import zosma.model.Timeslot;

public class TimeslotDao {

	Connection conn;

	public TimeslotDao() {
		try {
			conn = ConnectionFactory.getConnection();
		}catch (Exception e){
			conn = null;
		}
	}

	private Timeslot genTimeslot(ResultSet rs) throws Exception{
		String id = rs.getString("slotID");
		LocalDateTime time = LocalDateTime.parse(rs.getString("time"));
		boolean state = rs.getBoolean("state");

		Meeting meeting = (new MeetingDao()).getMeeting(id);
		Timeslot t = new Timeslot();

		t.setID(id);
		t.setTM(time);
		t.setST(state);
		t.setME(meeting);
		return t;
	}

	public Timeslot getTimeslot(String id) throws Exception{
		try {
			Timeslot slot = null;
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM slot WHERE slotID=?;");
			ps.setString(1, id);
			ResultSet resultSet = ps.executeQuery();

			while (resultSet.next()) {
				slot = genTimeslot(resultSet);
			}
			resultSet.close();
			ps.close();

			return slot;

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("getTimeslot failed finding Timeslot: " + e.getMessage());
		}
	}
	public ArrayList<Timeslot> getTimeslotByDay(String id) throws Exception {

		ArrayList<Timeslot> slots = new ArrayList<>();
		try {
			PreparedStatement ps =  conn.prepareStatement("SELECT * FROM slot WHERE dayID=?;");
			ps.setString(1,  id);
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				Timeslot slot = genTimeslot(rs);
				slots.add(slot);
			}
			rs.close();
			ps.close();
			return slots;
		} catch (Exception e) {
			throw new Exception("getTimeslotByDay failed finding Timeslots: " + e.getMessage());
		}
	}

	public boolean deleteTimeslot(Timeslot slot) throws Exception {
		try {
			PreparedStatement ps = conn.prepareStatement("DELETE FROM slot WHERE slotID=?;");
			ps.setString(1, slot.getid());
			int numAffected = ps.executeUpdate();
			ps.close();

			return (numAffected == 1);

		} catch (Exception e) {
			throw new Exception("deleteDay failed to delete Timeslot: " + e.getMessage());
		}
	}

	public boolean deleteTimeslotByDay(String id) throws Exception {
		try {
			PreparedStatement ps =  conn.prepareStatement("SELECT * FROM slot WHERE dayID=?;");
			ps.setString(1,  id);
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				Timeslot slot = genTimeslot(rs);
				MeetingDao meetingdao = new MeetingDao();
				meetingdao.deleteMeeting(slot.getMeeting());
				deleteTimeslot(slot);
			}
			rs.close();
			ps.close();
			return true;
		} catch (Exception e) {
			throw new Exception("deleteTimeslotByDay failed to delete Timeslot: " + e.getMessage());
		}
	}

	public boolean updateTimeslot(String dayid, Timeslot slot) throws Exception {
		try {
			PreparedStatement ps = conn.prepareStatement("UPDATE slot SET dayID=?, time=?, state=? WHERE slotID=?;");
			ps.setString(4, slot.getID());
			ps.setString(1, dayid);
			ps.setString(2, slot.getTM().toString());
			ps.setBoolean(3, slot.getST());

			MeetingDao meetingdao = new MeetingDao();
			if(slot.getMeeting() != null) {
				meetingdao.updateMeeting(slot.getMeeting());
			}

			int numAffected = ps.executeUpdate();
			ps.close();

			return (numAffected == 1);
		} catch (Exception e) {
			throw new Exception("updateTimeslot failed to update Timeslot: " + e.getMessage());
		}
	}

	public boolean addTimeslot(String dayid, Timeslot slot) throws Exception {
		try {
			PreparedStatement ps = conn.prepareStatement("INSERT INTO slot VALUES (?, ?, ?, ?)");
			ps.setString(1, slot.getID());
			ps.setString(2, dayid);
			ps.setString(3, slot.getTM().toString());
			ps.setBoolean(4, slot.getST());
			MeetingDao meetingdao = new MeetingDao();
			meetingdao.addMeeting(slot.getMeeting());
			ps.execute();
			return true;
		}catch (Exception e){
			throw new Exception("addTimeslot failed to add Timeslot: " + e.getMessage());
		}
	}
}