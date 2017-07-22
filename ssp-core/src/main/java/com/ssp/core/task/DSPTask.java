package com.ssp.core.task;

import com.ssp.api.dto.DSPResponse;
import com.ssp.api.entity.jpa.DSPDetail;
import com.ssp.api.exception.QPSLimitOverFlowException;
import com.ssp.core.util.SSPBean;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/18/17
 * Time: 10:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class DSPTask implements Callable<DSPResponse> {

    private static Logger logger = LoggerFactory.getLogger(DSPTask.class);

    private SSPBean sspBean;
    private DSPDetail dspDetail;
    private JSONObject content;

    public DSPTask(SSPBean sspBean, DSPDetail dspDetail, JSONObject content){
        this.sspBean = sspBean;
        this.dspDetail = dspDetail;
        this.content = content;
    }

    @Override
    public DSPResponse call() throws Exception {
        try{
            /*if(null != dspDetail.getQps() && dspDetail.getQps()>0)
                sspBean.getQpsCounter().increase(dspDetail.getId(), dspDetail.getQps());
            DSPResponse response = sspBean.getDspService().dspBid(dspDetail, content);*/
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            DSPResponse response = new DSPResponse();
            response.setCode(200);
            response.setDspDetail(dspDetail);
            response.setResponse("Success");
            response.setBidValue((double) dspDetail.getId() * 2.2);
            sspBean.getQpsCounter().decrease(dspDetail.getId());
            return  response;
        }catch (QPSLimitOverFlowException ex){
           logger.info("DSP PS limit exceeded. "+ dspDetail.getId() + dspDetail.getName());
        }
        return null;
    }
}
