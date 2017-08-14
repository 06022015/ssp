package com.ssp.core.impl;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.ssp.api.Constant;
import com.ssp.api.dto.BidData;
import com.ssp.api.dto.DSPResponse;
import com.ssp.api.entity.jpa.AdBlockInfo;
import com.ssp.api.entity.jpa.DSPInfo;
import com.ssp.api.entity.jpa.WinNoticeEntity;
import com.ssp.api.exception.SSPException;
import com.ssp.api.service.SSPService;
import com.ssp.core.task.DSPNotifyTask;
import com.ssp.core.task.DSPTask;
import com.ssp.core.util.SSPBean;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
        Long pubId = Long.parseLong(parameter.get(Constant.PUBLISHER_ID));
        AdBlockInfo adBlockInfo = sspBean.getJpaRepository().getAdBlockInfo(pubId, Long.parseLong(parameter.get(Constant.BLOCK_ID)));
        logger.debug("Publisher detail := "+adBlockInfo.toString());
        CityResponse location = getLocation(parameter.get(Constant.IP));
        logger.debug("Geo location:= "+location.toString());
        parameter.put(Constant.LMT, sspBean.getProperties().getProperty("ssp.lmt"));
        parameter.put(Constant.CURRENCY, sspBean.getProperties().getProperty("ssp.currency"));
        BidRequest bidRequest = sspBean.getRtbGenerator().generate(adBlockInfo,location, parameter);
        String requestContent = sspBean.getRtbGenerator().getBidAsString(bidRequest);
        logger.debug("Bid request:= "+requestContent);
        //this.sspBean.getMongoRepository().saveRTBJSON(requestContent);
        //logger.debug("rtb saved in mongo ");
        List<DSPInfo> dspInfos = sspBean.getJpaRepository().getAllDSP(adBlockInfo.getAdFormat());
        logger.debug("dspinfo "+dspInfos);
        Integer maxResponseTime = Integer.parseInt(sspBean.getProperties().getProperty(Constant.DSP_MAX_RESPONSE_PROP));
        List<Future<DSPResponse>> taskList = new ArrayList<Future<DSPResponse>>();
        JSONObject rtbJSON = parseRTBRequestAsJSON(requestContent);
        JSONArray dspIds = new JSONArray();
        for(DSPInfo dspInfo : dspInfos){
            dspInfo.setMaxResponseTime(maxResponseTime);
            DSPTask dspRequestTask = new DSPTask(sspBean,bidRequest, dspInfo, requestContent);
            Future<DSPResponse> task = sspBean.getDspExecutor().submit(dspRequestTask);
            taskList.add(task);
            dspIds.add(dspInfo.getUserId());
        }
        logger.debug("called dsp ");
        rtbJSON.put("dsp", dspIds);
        logger.debug("Saving rtb request");
        this.sspBean.getMongoRepository().saveRTBJSON(rtbJSON.toJSONString());
        logger.debug("Saved rtb request");
        Double maxValue = -1.0;
        DSPResponse winningDSP = null;
        JSONObject dspResponsesAsJSON = new JSONObject();
        dspResponsesAsJSON.put("request_id", bidRequest.getId());
        for(Future<DSPResponse> future : taskList){
            try {
                //DSPResponse dspResponse = future.get(maxResponseTime+10, TimeUnit.MILLISECONDS);
                DSPResponse dspResponse = future.get();
                dspResponsesAsJSON.put(dspResponse.getDspInfo().getUserId(), dspResponse.getResponseAsJSON());
                if(dspResponse.getCode() == HttpStatus.OK.value()){
                    if(dspResponse.getBidData().getAuctionPrice()>maxValue){
                        maxValue = dspResponse.getBidData().getAuctionPrice();
                        winningDSP = dspResponse;
                    }
                }
            } catch (InterruptedException e) {
                logger.error("Thread interrupted. "+ e.getMessage());
            } catch (ExecutionException e) {
                logger.error("dsp task execution task error:- "+ e.getMessage());
            } /*catch (TimeoutException e) {
                logger.error("dsp response time out ignoring it:- error message:- " + e.getMessage());
            }*/
        }
        logger.debug("read dsp response");
        logger.debug("Saving dsp response");
        this.sspBean.getMongoRepository().saveDSPResponse(dspResponsesAsJSON.toJSONString());
        logger.debug("Saved dsp response");
        if(null != winningDSP){
            logger.debug("notifying will URL");
            DSPNotifyTask notifyTask = new DSPNotifyTask(sspBean,winningDSP.getBidData().getFullNURL());
            sspBean.getDspNotifyExecutor().submit(notifyTask);
            saveWinningBid(winningDSP, adBlockInfo, pubId.intValue());
        }
        BidData bidData = null;
        if(null != winningDSP){
            bidData = winningDSP.getBidData();
            bidData.setWidth(adBlockInfo.getWidth());
            bidData.setHeight(adBlockInfo.getHeight());
        }
        return bidData;
    }

    private JSONObject parseRTBRequestAsJSON(String rtbRequest)  {
        JSONParser parser = new JSONParser();
        try {
            return  (JSONObject) parser.parse(rtbRequest);
        } catch (ParseException e) {
            logger.error("Unable to convert RTB request as JSONObject");
            throw new SSPException(500,"Unable to convert RTB request as JSONObject");
        }
    }

    private void saveWinningBid(DSPResponse dspResponse, AdBlockInfo adBlockInfo, Integer pubId){
        WinNoticeEntity winNotice = new WinNoticeEntity();
        winNotice.setDspId(dspResponse.getDspInfo().getUserId().intValue());
        winNotice.setPublisherId(pubId);
        winNotice.setPublisherShare(adBlockInfo.getFloorPrice());
        winNotice.setDspBidAmount(dspResponse.getBidData().getAuctionPrice().floatValue());
        winNotice.setRequestId(dspResponse.getBidData().getAuctionId());
        this.sspBean.getJpaRepository().saveWinningBid(winNotice);
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
