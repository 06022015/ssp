package com.ssp.core.util;

import com.google.common.util.concurrent.RateLimiter;
import com.ssp.api.exception.QPSLimitOverFlowException;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import java.util.Calendar;
import java.util.Date;
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
@Singleton
public class DSPQPSCounter {

    private  Map<Long, Integer> qpsCounter;

    private static Map<Long, QPSCounter> dspRateLimit;

    public DSPQPSCounter(){
        this.qpsCounter = new ConcurrentHashMap<Long, Integer>();
        this.dspRateLimit = new ConcurrentHashMap<Long, QPSCounter>();
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

    public boolean checkQPS(Long dspId, Integer maxCount){
         QPSCounter qpsCounter = dspRateLimit.get(dspId);
         if(null == qpsCounter)
             qpsCounter = new QPSCounter();
         int currentSecond = getCurrentSecond();
         if(qpsCounter.timeInSecond == currentSecond){
             if(qpsCounter.count < maxCount){
                qpsCounter.count = qpsCounter.count+1;
             }else
                throw new QPSLimitOverFlowException(503 ,"DSP QPS Limit exceeded");
         }else{
             qpsCounter.timeInSecond  =  currentSecond;
             qpsCounter.count = 1;
         }
        dspRateLimit.put(dspId, qpsCounter);
        return true;
    }

    private int getCurrentSecond(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        return cal.get(Calendar.SECOND);
    }

    static class QPSCounter{
        int timeInSecond =0;
        int count = 0;
    }

    public static void main(String args[]) throws InterruptedException {
        DSPQPSCounter dspqpsCounter = new DSPQPSCounter();
        for(int i=0;i< 3;i++){
            System.out.println(dspqpsCounter.checkQPS(1L, 2));
            Thread.sleep(200);
        }

    }
}
