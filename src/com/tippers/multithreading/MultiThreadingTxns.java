package com.tippers.multithreading;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.tippers.dbutil.ConnectToMySql;
import com.tippers.dbutil.ConnectToPostgres;
import com.tippers.dbutil.Properties;

public class MultiThreadingTxns {
	
	static int commitCount = 0;
	
	public static int getCommitCount() {
		return commitCount;
	}
	
	public static void incrementCommitCount() {
		commitCount+=1;
	}
	
	public static void resetCommitCount() {
		commitCount = 0;
	}

	protected static Queue<String> jobs = new LinkedList<String>();

	synchronized public static boolean isJobQueueEmpty() {
		return jobs.isEmpty();
	}
	
	synchronized public static String getJobToExecute() {
		if(!isJobQueueEmpty()) {
			return jobs.poll();
		}
		else return null;
	}

	private static void readInput(Queue<String> jobs) {
		try {
			File f = new File(Properties.INPUT_FILE);
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line = null;
			int queryCount = 1;
			StringBuilder sb = new StringBuilder();

			while ((line = br.readLine()) != null) {
				if(line.contains("SELECT")) {
					jobs.add(line);
					continue;
				}
				sb.append(line);
				if(queryCount < Properties.OPERATIONS_PER_TXN) {
					queryCount++;
					continue;
				}
				jobs.add(sb.toString());
				sb.setLength(0);
				queryCount = 1;
			}
			if(sb.length()!=0)
				jobs.add(sb.toString());
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public static void main(String args[]) throws InterruptedException{

		//read file line by line and push each line into a queue
		//TODO push X number of operations from queue to the threads. X operations = 1 transaction
		//TODO figure out how each thread will obtain the X number of operations

		//initializeDB();
		readInput(jobs);

		//RejectedExecutionHandler implementation
		RejectedExecutionHandlerImpl rejectionHandler = new RejectedExecutionHandlerImpl();
		//Get the ThreadFactory implementation to use
		ThreadFactory threadFactory = Executors.defaultThreadFactory();
		//creating the ThreadPoolExecutor
		ThreadPoolExecutor executorPool = new ThreadPoolExecutor(Properties.CORE_POOL_SIZE, Properties.MPL_LEVEL, Properties.KEEP_ALIVE_TIME, TimeUnit.SECONDS,
				new ArrayBlockingQueue<Runnable>(1000000), threadFactory, rejectionHandler);

		//start the monitoring thread
		MonitorThreads monitor = new MonitorThreads(executorPool, 1);
		Thread monitorThread = new Thread(monitor);
		monitorThread.start();
		//submit work to the thread pool
		int i=0;
		while(!jobs.isEmpty()){
			++i;
			executorPool.execute(new WorkerThread("Thread_" + i, getJobToExecute()));
		}
		
		Thread.sleep(1000000);
		//shut down the pool
		executorPool.shutdown();
		//shut down the monitor thread
		Thread.sleep(5000);
		monitor.shutdown();

	}

	private static void initializeDB() {
		Connection c;
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
			
			Statement stmt = c.createStatement();
			stmt.executeUpdate(Properties.SET_DB_PARAMS);
			stmt.close();
			c.commit();
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}

