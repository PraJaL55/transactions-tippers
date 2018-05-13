package com.tippers.multithreading;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.tippers.dbutil.ConnectToMySql;
import com.tippers.dbutil.ConnectToPostgres;
import com.tippers.dbutil.Properties;

public class WorkerThread implements Runnable {
	String threadName;
	String queryString;
	Connection c;
	MultiThreadingTxns mtt = new MultiThreadingTxns();

	public WorkerThread(String name, String string){
		this.threadName = name;
		this.queryString = string;
		
	}

	@Override
	public void run() {
		try {
			
			//queryString = MultiThreadingTxns.getJobToExecute();
//			while(queryString.startsWith("S")) {
//				queryString = mtt.getJobToExecute();
//			}
			try {
				if(Properties.CONNECT_TO_PSQL) {
					ConnectToPostgres psqlConnection = new ConnectToPostgres();
					c = psqlConnection.getConnectionToDB(Properties.AUTO_COMMIT);
				}
				else {
					//connectToMysql();
					ConnectToMySql mysqlConnection = new ConnectToMySql();
					c = mysqlConnection.getConnectionToDB(Properties.AUTO_COMMIT);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			Statement stmt = c.createStatement();

			if(this.queryString!=null) {
				if(this.queryString.charAt(0) == 'I') { //INSERT Statement
					stmt.executeUpdate(this.queryString);
				} else { //SELECT Query
					ResultSet rs = stmt.executeQuery(this.queryString.substring(0, this.queryString.indexOf(";") +1));
					rs.close();
				}
			}
			stmt.close();
			c.commit();
			c.close();
			MultiThreadingTxns.incrementCommitCount();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString(){
		return this.threadName;
	}
}

