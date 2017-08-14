package com.ssp.core.task;

import com.ssp.api.dto.DSPResponse;
import com.ssp.api.entity.jpa.DSPInfo;
import com.ssp.api.exception.QPSLimitOverFlowException;
import com.ssp.api.exception.SSPURLException;
import com.ssp.core.util.SSPBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/22/17
 * Time: 12:50 AM
 * To change this template use File | Settings | File Templates.
 */
public class DSPNotifyTask implements Callable<Integer> {

    private static Logger logger = LoggerFactory.getLogger(DSPTask.class);

    private SSPBean sspBean;
    private String notifyURL;

    public DSPNotifyTask(SSPBean sspBean, String notifyURL){
        this.sspBean = sspBean;
        this.notifyURL = notifyURL;
    }

    public Integer call() throws Exception {
        try{
            logger.debug("Notifying winning bid:- "+ notifyURL);
            return sspBean.getDspService().notifyDSP(notifyURL);
        }catch (SSPURLException ex){
            logger.error("Notify URL issue:- "+notifyURL + " message:- " + ex.getMessage());
        }
        return null;
    }
}
