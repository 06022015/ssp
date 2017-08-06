package com.ssp.api.monitor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 6/25/17
 * Time: 10:09 PM
 * To change this template use File | Settings | File Templates.
 */
public  interface ThreadPoolMonitorService extends Runnable{


    abstract ExecutorServiceStatus monitor();

    ThreadPoolExecutor getExecutor();

}
