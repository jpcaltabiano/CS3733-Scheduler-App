package org.json.simple.parser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


//Connection to Database

public class ConnectionFactory {
	//Not our URL switch to correct one
	public final static String URL = "jdbc:mysql://localhost:3306/testdb";
	public final static String USER = "testuser";
	public final static String PASS = "testpass";
	
	public final static String jdbcTag = "jdbc:mysql://";
	////Change port to database --------------
	public final static String rdsMySqlDatabasePort = "3306";
	public final static String multiQueries = "?allowMultiQueries=true";
	
	/// 
	public final static String dbName = "schedulerdb";
	
	static Connection conn;
	
	public static Connection getConnection() throws Exception {
		if(conn != null) {
			return conn;
		}
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbcTag + URL + ":" + rdsMySqlDatabasePort + "/" + dbName + multiQueries,
					USER,
					PASS);
			return conn;
		}catch(Exception e) {
			throw new RuntimeException("Error connecting to the databse.", e);
		}
	}
	
	//Test Connection
	
	/*
    public static void main(String[] args) {
        Connection connection = ConnectionFactory.getConnection();
    }
    */
}
