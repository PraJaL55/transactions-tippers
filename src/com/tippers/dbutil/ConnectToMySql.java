package com.tippers.dbutil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectToMySql {

	public Connection getConnectionToDB(boolean autoCommit) throws SQLException {
		Connection con;
		try{
			Class.forName("com.mysql.cj.jdbc.Driver");
			con=DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/tdm_project","root","");
			//here sonoo is database name, root is username and password
			
		}catch(Exception e){
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+e.getMessage());
			System.exit(0);
			return null;
		}
		System.out.println("Opened database successfully");
		con.setAutoCommit(autoCommit);
		return con;
	}
}
