package com.tippers.dbutil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectToMySql {

	public Connection getConnectionToDB(boolean autoCommit) throws SQLException {
		Connection con;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			con=DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/" + Properties.MYSQL_DB_NAME + "?useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC","root","password");
			
		}catch(Exception e){
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+e.getMessage());
			System.exit(0);
			return null;
		}
		con.setAutoCommit(autoCommit);
		return con;
	}
}
