package com.tippers.dbutil;

public final class Properties {
	public static final String SET_DB_PARAMS = "\n" + 
			"SET statement_timeout = 0;\n" + 
			"SET lock_timeout = 0;\n" + 
			"SET idle_in_transaction_session_timeout = 0;\n" + 
			"SET client_encoding = 'UTF8';\n" + 
			"SET standard_conforming_strings = on;\n" + 
			"SET check_function_bodies = false;\n" + 
			"SET client_min_messages = warning;\n" + 
			"SET row_security = off;\n" + 
			"\n" + 
			"SET search_path = public, pg_catalog;";
	
	public static final boolean AUTO_COMMIT = false; 
	public static final int CORE_POOL_SIZE = 0; //should always be less than equal to MPL_LEVEL
	public static final int MPL_LEVEL = 1;
	public static final int KEEP_ALIVE_TIME = 10;
	public static final boolean CONNECT_TO_PSQL = true; //if false it will connect to MySQL
	public static final int OPERATIONS_PER_TXN = 1; //change this value to increase operation counts
	public static final String INPUT_FILE = "/Users/Jamshir-MAC/Documents/Everything UC Irvine/Spring 2018/CS 223 TDM/Project/Part 1/project1/low_concurrency_final_sorted/8-Nov-2017.sql";
}
