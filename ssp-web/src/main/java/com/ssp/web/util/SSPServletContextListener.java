package com.ssp.web.util;

import com.ssp.api.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.TimeZone;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/21/17
 * Time: 10:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class SSPServletContextListener implements ServletContextListener {
    private static Logger logger = LoggerFactory.getLogger(SSPServletContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        logger.info("Initialized... ");
        TimeZone.setDefault(TimeZone.getTimeZone(Constant.TIME_ZONE));
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor)ApplicationContextUtil.getApplicationContext().getBean("dspExecutor");
        executor.shutdown();
        executor.destroy();
        logger.info("Destroying Context...");
        try {
            logger.info("Calling MySQL AbandonedConnectionCleanupThread shutdown");
            com.mysql.jdbc.AbandonedConnectionCleanupThread.shutdown();

        } catch (InterruptedException e) {
            logger.error("Error calling MySQL AbandonedConnectionCleanupThread shutdown {}", e);
        }
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            if (driver.getClass().getClassLoader() == cl) {
                try {
                    logger.info("Deregistering JDBC driver {}", driver);
                    DriverManager.deregisterDriver(driver);

                } catch (SQLException ex) {
                    logger.error("Error deregistering JDBC driver {}", driver, ex);
                }
            } else {
                logger.trace("Not deregistering JDBC driver {} as it does not belong to this webapp's ClassLoader", driver);
            }
        }
    }
}
