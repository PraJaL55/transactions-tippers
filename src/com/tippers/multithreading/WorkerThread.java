package com.tippers.multithreading;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.tippers.dbutil.ConnectToMySql;
import com.tippers.dbutil.ConnectToPostgres;
import com.tippers.dbutil.Properties;

public class WorkerThread implements Runnable {
	String queryString;
	Connection c;
	MultiThreadingTxns mtt = new MultiThreadingTxns();

	public WorkerThread(){
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			
			queryString = MultiThreadingTxns.getJobToExecute();
//			while(queryString.startsWith("S")) {
//				queryString = mtt.getJobToExecute();
//			}

			if(queryString!=null) {
				System.out.println(Thread.currentThread().getName()+" Start. Query = " + queryString);
				//write code to execute the queries
				Statement stmt = c.createStatement();
				stmt.executeUpdate(queryString);
				//stmt.close();
			}
			
			if(!MultiThreadingTxns.isJobQueueEmpty()){
				this.run();
			}
			
			c.commit();
			c.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName()+" End.");
	}

	@Override
	public String toString(){
		return this.queryString;
	}
}

