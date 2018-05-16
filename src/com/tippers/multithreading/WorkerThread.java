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
	static ConnectToMySql mysqlConnection = new ConnectToMySql();
	Connection c = null;
	MultiThreadingTxns mtt = new MultiThreadingTxns();

	public WorkerThread(String name, String string){
		this.threadName = name;
		this.queryString = string;
		//		try {
		//			if(null==c) 
		//				c = mysqlConnection.getConnectionToDB(Properties.AUTO_COMMIT);
		//		} catch (SQLException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
	}

	@Override
	public void run() {
		try {
			if(Properties.CONNECT_TO_PSQL) {
				ConnectToPostgres psqlConnection = new ConnectToPostgres();
				c = psqlConnection.getConnectionToDB(Properties.AUTO_COMMIT);
				c.setTransactionIsolation(Properties.TXN_ISOLATION);

				Statement stmt = c.createStatement();

				if(this.queryString!=null) {
					if(this.queryString.charAt(0) == 'I') { //INSERT Statement
						stmt.executeUpdate(this.queryString);
					} else { //SELECT Query
						ResultSet rs = stmt.executeQuery(this.queryString.substring(0, this.queryString.indexOf(";")));
						rs.close();
					}
				}
				stmt.close();
				c.commit();
				c.close();
			}
			else {
				//connectToMysql();
				ConnectToMySql mysqlConnection = new ConnectToMySql();
				c = mysqlConnection.getConnectionToDB(Properties.AUTO_COMMIT);
				c.setTransactionIsolation(Properties.TXN_ISOLATION);
				Statement stmt = c.createStatement();

				if(this.queryString!=null) {
					if(this.queryString.charAt(0) == 'I') { //INSERT Statement
						String[] queryBatch = this.queryString.split(";");
						for(String query: queryBatch) {
							stmt.addBatch(query + ";");
						}
						stmt.executeBatch();
					} else { //SELECT Query
						if(!this.queryString.contains("date_trunc")) {
							ResultSet rs = stmt.executeQuery(this.queryString.substring(0, this.queryString.indexOf(";")));
							rs.close();
						}
					}
				}
				stmt.close();
				c.commit();
				c.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		MultiThreadingTxns.incrementCommitCount();
	}

	@Override
	public String toString(){
		return this.threadName;
	}
}

