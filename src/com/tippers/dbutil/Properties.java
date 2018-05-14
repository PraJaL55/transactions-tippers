package com.tippers.dbutil;

public final class Properties {
	public static final String SET_DB_PARAMS = "SET statement_timeout = 100;" + 
			"SET lock_timeout = 100;" + 
			"SET idle_in_transaction_session_timeout = 100;" + 
			"SET client_encoding = 'UTF8';" + 
			"SET standard_conforming_strings = on;" + 
			"SET check_function_bodies = false;" + 
			"SET client_min_messages = warning;" + 
			"SET row_security = off;" +
			"SET search_path = public, pg_catalog;";
	
	public static final boolean AUTO_COMMIT = false;
	public static final int CORE_POOL_SIZE = 50; //should always be less than equal to MPL_LEVEL
	public static final int MPL_LEVEL = 50;
	public static final int KEEP_ALIVE_TIME = 10;
	public static final boolean CONNECT_TO_PSQL = false; //if false it will connect to MySQL
	public static final int OPERATIONS_PER_TXN = 10; //change this value to increase operation counts
	public static final String INPUT_FILE = "/Users/Jamshir-MAC/Documents/Everything UC Irvine/Spring 2018/CS 223 TDM/Project/Part 1/project1/low_concurrency_final_sorted/8-Nov-2017.sql";
}
