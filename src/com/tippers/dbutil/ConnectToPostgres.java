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
					.getConnection("jdbc:postgresql://localhost:5432/" + Properties.POSTGRES_DB_NAME);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+e.getMessage());
			System.exit(0);
			return null;
		}
		c.setAutoCommit(autoCommit);
		return c;
	}
	
	public void closeConnectionToDB() throws SQLException {
		c.close();
	}
}