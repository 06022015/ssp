package com.ssp.web.monitor;

import com.ssp.api.monitor.ExecutorServiceStatus;
import com.ssp.api.monitor.ThreadPoolMonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 6/25/17
 * Time: 10:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class ThreadPoolMonitorServiceImpl implements ThreadPoolMonitorService {

    private static Integer WAIT_TIME = 1000*10;

    private static Logger logger = LoggerFactory.getLogger(ThreadPoolMonitorServiceImpl.class);

    private Integer waitTime;

    private ThreadPoolTaskExecutor executor;

    public ThreadPoolMonitorServiceImpl(ThreadPoolTaskExecutor executor) {
        this.executor = executor;
        this.waitTime = WAIT_TIME;
    }

    public Integer getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(Integer waitTime) {
        this.waitTime = waitTime;
    }

    @Override
    public ExecutorServiceStatus monitor() {
        ExecutorServiceStatus status = new ExecutorServiceStatus();
        status.setPoolSize(executor.getPoolSize());
        status.setCorePoolSize(executor.getCorePoolSize());
        status.setMaximumPoolSize(executor.getMaxPoolSize());
        status.setActiveCount(executor.getActiveCount());
        status.setCompletedTaskCount(executor.getThreadPoolExecutor().getCompletedTaskCount());
        status.setTaskCount(executor.getThreadPoolExecutor().getTaskCount());
        status.setTerminated(executor.getThreadPoolExecutor().isTerminated());
        logger.debug(status.toString());
        return status;
    }

    @Override
    public ThreadPoolExecutor getExecutor() {
        return this.executor.getThreadPoolExecutor();
    }

    @Override
    public void run() {
        try {
            monitor();
            Thread.sleep(getWaitTime());
        } catch (InterruptedException e) {
            logger.error("Thread pool monitor service interupted" + e.getMessage());
        }
    }
}
