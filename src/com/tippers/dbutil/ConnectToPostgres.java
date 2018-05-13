package com.tippers.dbutil;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectToPostgres {
	Connection c = null;

	public Connection getConnectionToDB(boolean autoCommit) throws SQLException {
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager
					.getConnection("jdbc:postgresql://localhost:5432/tdm_project");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+e.getMessage());
			System.exit(0);
			return null;
		}
		System.out.println("Opened database successfully");
		c.setAutoCommit(autoCommit);
		return c;
	}
	
	public void closeConnectionToDB() throws SQLException {
		c.close();
	}
}