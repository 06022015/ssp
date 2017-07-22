package com.ssp.web.servlet;

import com.ssp.api.dto.PublisherRequest;
import com.ssp.api.dto.PublisherResponse;
import com.ssp.api.monitor.ThreadPoolMonitorService;
import com.ssp.api.service.SSPService;
import com.ssp.web.monitor.ThreadPoolMonitorServiceImpl;
import com.ssp.web.util.ApplicationContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.servlet.http.HttpServlet;
import java.util.Calendar;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 6/24/17
 * Time: 3:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class SSPServlet extends HttpServlet{

    private static Logger logger = LoggerFactory.getLogger(SSPServlet.class);

    public void doPost(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp) throws javax.servlet.ServletException, java.io.IOException {
        PublisherRequest request = new PublisherRequest();
        String pubId = req.getParameter("pubId");
        request.setPublisherId(pubId);
        long startTime = Calendar.getInstance().getTimeInMillis();
        logger.info("Request in for pubId:-" + pubId +" "+ startTime);
        SSPService service = (SSPService)ApplicationContextUtil.getApplicationContext().getBean("sspService");
        logger.info("Reading service obj:-"+ (Calendar.getInstance().getTimeInMillis() - startTime));

        PublisherResponse response = service.processRequest(request);
        resp.getWriter().write(response.getResponse());
        logger.info("Request out for pubId:-" + pubId +" "+ (Calendar.getInstance().getTimeInMillis()-startTime));
    }
    public void doGet(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp) throws javax.servlet.ServletException, java.io.IOException {
        ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor)ApplicationContextUtil.getApplicationContext().getBean("dspExecutor");
        ThreadPoolMonitorService monitor  = new ThreadPoolMonitorServiceImpl(executor);
        resp.getWriter().write(monitor.monitor().toString());
    }

}
