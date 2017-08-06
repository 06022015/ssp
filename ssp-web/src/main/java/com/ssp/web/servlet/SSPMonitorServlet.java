package com.ssp.web.servlet;

import com.ssp.api.monitor.ThreadPoolMonitorService;
import com.ssp.web.monitor.ThreadPoolMonitorServiceImpl;
import com.ssp.web.util.ApplicationContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.servlet.http.HttpServlet;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 6/24/17
 * Time: 3:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class SSPMonitorServlet extends HttpServlet {

    private static Logger logger = LoggerFactory.getLogger(SSPMonitorServlet.class);

    public void doGet(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp) throws javax.servlet.ServletException, java.io.IOException {
        ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor)ApplicationContextUtil.getApplicationContext().getBean("dspExecutor");
        ThreadPoolMonitorService monitor  = new ThreadPoolMonitorServiceImpl(executor);
        resp.getWriter().write(monitor.monitor().toString());
    }

}
