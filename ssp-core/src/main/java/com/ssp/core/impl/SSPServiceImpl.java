package com.ssp.core.impl;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.ssp.api.Constant;
import com.ssp.api.dto.BidData;
import com.ssp.api.dto.DSPResponse;
import com.ssp.api.entity.jpa.AdBlockInfo;
import com.ssp.api.entity.jpa.DSPInfo;
import com.ssp.api.exception.SSPException;
import com.ssp.api.service.SSPService;
import com.ssp.core.task.DSPNotifyTask;
import com.ssp.core.task.DSPTask;
import com.ssp.core.util.SSPBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.google.openrtb.OpenRtb.BidRequest;

import javax.transaction.Transactional;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/18/17
 * Time: 11:16 PM
 * To change this template use File | Settings | File Templates.
 */
@Service("sspService")
@Transactional
public class SSPServiceImpl implements SSPService{

    private static Logger logger = LoggerFactory.getLogger(SSPServiceImpl.class);

    @Autowired
    private SSPBean sspBean;

    public BidData processRequest(Map<String,String> parameter) throws SSPException , IOException {
        AdBlockInfo adBlockInfo = sspBean.getJpaRepository().getAdBlockInfo(Long.parseLong(parameter.get(Constant.PUBLISHER_ID)),
                Long.parseLong(parameter.get(Constant.BLOCK_ID)));
        logger.info("Publisher detail := "+adBlockInfo.toString());
        CityResponse location = getLocation(parameter.get(Constant.IP));
        logger.info("Geo location:= "+location.toString());
        BidRequest bidRequest = sspBean.getRtbGenerator().generate(adBlockInfo,location, parameter);
        String requestContent = sspBean.getRtbGenerator().getBidAsString(bidRequest);
        logger.info("Bid request:= "+requestContent);
        this.sspBean.getMongoRepository().saveRTBJSON(requestContent);
        logger.info("rtb saved in mongo ");
        List<DSPInfo> dspInfos = sspBean.getJpaRepository().getAllDSP(adBlockInfo.getAdFormat());
        logger.info("dspinfo "+dspInfos);
        Integer maxResponseTime = Integer.parseInt(sspBean.getProperties().getProperty(Constant.DSP_MAX_RESPONSE_PROP));
        List<Future<DSPResponse>> taskList = new ArrayList<Future<DSPResponse>>();
        for(DSPInfo dspInfo : dspInfos){
            dspInfo.setMaxResponseTime(maxResponseTime);
            DSPTask dspRequestTask = new DSPTask(sspBean,bidRequest, dspInfo, requestContent);
            Future<DSPResponse> task = sspBean.getDspExecutor().submit(dspRequestTask);
            taskList.add(task);
        }
        logger.info("called dsp ");
        Double maxValue = -1.0;
        DSPResponse winningDSP = null;
        for(Future<DSPResponse> future : taskList){
            try {
                DSPResponse dspResponse = future.get(maxResponseTime, TimeUnit.MILLISECONDS);
                if(null != dspResponse){
                    /*logger.info("saving  winning bid");
                    this.sspBean.getMongoRepository().saveWinningBid(dspResponse.getResponse());
                    logger.info("saved  winning bid");*/
                    if(dspResponse.getBidData().getAuctionPrice()>maxValue){
                        maxValue = dspResponse.getBidData().getAuctionPrice();
                        winningDSP = dspResponse;
                    }
                }
            } catch (InterruptedException e) {
                logger.info("Thread interrupted. "+ e.getMessage());
            } catch (ExecutionException e) {
                logger.error("dsp task execution task error:- "+ e.getMessage());
            } catch (TimeoutException e) {
                logger.error("dsp response time out ignoring it:- error message:- " + e.getMessage());
            }
        }
        logger.info("read dsp response");
        if(maxValue>=0.0){
            DSPNotifyTask notifyTask = new DSPNotifyTask(sspBean,winningDSP.getBidData().getFullNURL());
            sspBean.getDspNotifyExecutor().submit(notifyTask);
            logger.info("saving  winning bid");
            //this.sspBean.getMongoRepository().saveWinningBid(winningDSP.getResponse());
            logger.info("saved  winning bid");
        }
        BidData bidData = null;
        if(null != winningDSP){
            bidData = winningDSP.getBidData();
        }
        return bidData;
    }

    private CityResponse getLocation(String ip){
        try {
            return sspBean.getLocationService().getLocation(ip);
        } catch (IOException  e) {
            throw new SSPException("Not able to read location from maxmind",e,HttpStatus.BAD_REQUEST.value());
        } catch (GeoIp2Exception e) {
            throw new SSPException("Not able to read location from maxmind",e,HttpStatus.BAD_REQUEST.value());
        }
    }
}
