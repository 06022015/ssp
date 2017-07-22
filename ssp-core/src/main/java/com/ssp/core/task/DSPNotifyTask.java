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
 * Date: 7/22/17
 * Time: 12:50 AM
 * To change this template use File | Settings | File Templates.
 */
public class DSPNotifyTask implements Callable<DSPResponse> {

    private static Logger logger = LoggerFactory.getLogger(DSPTask.class);

    private SSPBean sspBean;
    private DSPDetail dspDetail;
    private JSONObject content;

    public DSPNotifyTask(SSPBean sspBean, DSPDetail dspDetail, JSONObject content){
        this.sspBean = sspBean;
        this.dspDetail = dspDetail;
        this.content = content;
    }

    @Override
    public DSPResponse call() throws Exception {
        try{
            return sspBean.getDspService().notifyDSP(dspDetail, content);
        }catch (QPSLimitOverFlowException ex){
            logger.info("DSP PS limit exceeded. "+ dspDetail.getId() + dspDetail.getName());
        }
        return null;
    }
}
