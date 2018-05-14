package com.tippers.multithreading;

import java.util.concurrent.ThreadPoolExecutor;

public class MonitorThreads implements Runnable {
    private ThreadPoolExecutor executor;
    private int seconds;
    private boolean run=true;
	int secondspassed = 1;


    public MonitorThreads(ThreadPoolExecutor executor, int delay)
    {
        this.executor = executor;
        this.seconds=delay;
    }
    public void shutdown(){
        this.run=false;
        
    }
    @Override
    public void run()
    {
        while(run){
            System.out.println(
                    String.format("[monitor] [%d/%d] Active: %d, Completed: %d, Task: %d, isShutdown: %s, isTerminated: %s",
                            this.executor.getPoolSize(),
                            this.executor.getCorePoolSize(),
                            this.executor.getActiveCount(),
                            this.executor.getCompletedTaskCount(),
                            this.executor.getTaskCount(),
                            this.executor.isShutdown(),
                            this.executor.isTerminated()));
            float commitcount = MultiThreadingTxns.getCommitCount();
            System.out.println("*****Commit Count: " + commitcount + "\nTransactions/Second: " + commitcount/secondspassed);
//            MultiThreadingTxns.resetCommitCount();
            try {
                Thread.sleep(seconds*1000);
                secondspassed++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
    }
}
