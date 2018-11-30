package org.json.simple.parser;

import java.sql.Connection;
import com.mysql.jdbc.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;


//Connection to Database

public class ConnectionFactory {
	//Not our URL switch to correct one
	public static final String URL = "jdbc:mysql://localhost:3306/testdb";
	
	public static final String USER = "testuser";
	public static final String PASS = "testpass";
	
	public static Connection getConnection() {
		try {
			DriverManager.registerDriver(new Driver());
			return DriverManager.getConnection(URL, USER, PASS);
		}catch(SQLException ex) {
			throw new RuntimeException("Error connecting to the databse.", ex);
		}
	}
	
	//Test Connection
	

    public static void main(String[] args) {
        Connection connection = ConnectionFactory.getConnection();
    }
}

/*
DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
Connection conn = DriverManager.getConnection("jdbc:oracle:oci8:\r\n" + "@oracle.world");

public Schedule generateSchedule(ResultSet resultSet) {
	String nm = resultSet.getString("nm");
	String sDate = resultSet.getString("sDate");
	String eDate = resultSet.getString("eDate");
	String sHour = resultSet.getString("sHour");
	String eHour = resultSet.getString("eHour");
	int dur = resultSet.getInt("dur");
	return new Schedule(nm, sDate, eDate, sHour, eHour, dur);
}

public String getSchedule(String name) {
	try {
		Schedule schedule = null;
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM Constants WHERE name=?;");
		ps.setString(1, name);
		ResultSet resultSet = ps.executeQuery();
		
		while(resultSet.next()) {
			schedule = generateSchedule(resultSet);
		}
	}catch(Exception e) {
		e.printStackTrace();
	}
}
*/