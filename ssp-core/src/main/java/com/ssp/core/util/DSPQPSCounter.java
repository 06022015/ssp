package com.ssp.core.util;

import com.ssp.api.exception.QPSLimitOverFlowException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/19/17
 * Time: 12:07 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class DSPQPSCounter {

    private  Map<Long, Integer> qpsCounter;

    public DSPQPSCounter(){
        this.qpsCounter = new ConcurrentHashMap<Long, Integer>();
    }

    public void increase(Long dspId, Integer maxCount)throws QPSLimitOverFlowException{
       Integer count = this.qpsCounter.get(dspId);
       if(null == count)
           count = 0;
       if(count < maxCount){
           this.qpsCounter.put(dspId, count+1);
       }else{
           throw new QPSLimitOverFlowException(503 ,"DSP QPS Limit exceeded");
       }
    }

    public void decrease(Long dspId) throws QPSLimitOverFlowException{
        Integer count = this.qpsCounter.get(dspId);
        if(null != count && count>0){
            this.qpsCounter.put(dspId,this.qpsCounter.get(dspId)-1);
        }
    }
}
