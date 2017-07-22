package com.ssp.core.impl;

import com.ssp.api.dto.DSPRequest;
import com.ssp.api.dto.DSPResponse;
import com.ssp.api.dto.PublisherRequest;
import com.ssp.api.dto.PublisherResponse;
import com.ssp.api.entity.jpa.DSPDetail;
import com.ssp.api.service.SSPService;
import com.ssp.core.task.DSPNotifyTask;
import com.ssp.core.task.DSPTask;
import com.ssp.core.util.SSPBean;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/18/17
 * Time: 11:16 PM
 * To change this template use File | Settings | File Templates.
 */
@Service("sspService")
public class SSPServiceImpl implements SSPService{

    private static Logger logger = LoggerFactory.getLogger(SSPServiceImpl.class);

    @Autowired
    private SSPBean sspBean;

    public PublisherResponse processRequest(PublisherRequest request){
        long startTime = Calendar.getInstance().getTimeInMillis();
        logger.info("Process in:-"+ startTime);
        JSONObject rtbContent = new JSONObject();
        List<DSPDetail> dspDetails = sspBean.getJpaRepository().getAllDSP();
        logger.info("Reading dsps in:-"+ (Calendar.getInstance().getTimeInMillis()-startTime));
        List<Future<DSPResponse>> taskList = new ArrayList<Future<DSPResponse>>();
        long execuTime = Calendar.getInstance().getTimeInMillis();
        for(DSPDetail dspDetail : dspDetails){
            DSPTask dspRequestTask = new DSPTask(sspBean, dspDetail, rtbContent);
            Future<DSPResponse> task = sspBean.getDspExecutor().submit(dspRequestTask);
            taskList.add(task);
        }
        long postExecuTime = Calendar.getInstance().getTimeInMillis();
        logger.info("Post execution service :-"+ (postExecuTime-execuTime));
        Double maxValue = 0.0;
        DSPResponse maxDSPResponse = null;
        for(Future<DSPResponse> future : taskList){
            try {
                DSPResponse dspResponse = future.get();
                if(dspResponse.getBidValue()>maxValue){
                    maxValue = dspResponse.getBidValue();
                    maxDSPResponse = dspResponse;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        logger.info("Execution complete :-"+ (Calendar.getInstance().getTimeInMillis()-postExecuTime));
        DSPNotifyTask notifyTask = new DSPNotifyTask(sspBean, maxDSPResponse.getDspDetail(), rtbContent);
        sspBean.getDspNotifyExecutor().submit(notifyTask);
        return null != maxDSPResponse? new PublisherResponse(maxDSPResponse.getResponse()): null;
    }

}
