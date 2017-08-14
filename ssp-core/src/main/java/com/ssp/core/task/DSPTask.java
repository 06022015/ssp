package com.ssp.core.task;

import com.google.openrtb.OpenRtb;
import com.google.openrtb.OpenRtb.BidRequest;
import com.ssp.api.dto.BidData;
import com.ssp.api.dto.DSPResponse;
import com.ssp.api.entity.jpa.DSPInfo;
import com.ssp.api.exception.QPSLimitOverFlowException;
import com.ssp.api.exception.SSPURLException;
import com.ssp.core.util.SSPBean;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.io.IOException;
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
    private DSPInfo dspInfo;
    private String content;
    private BidRequest bidRequest;

    public DSPTask(SSPBean sspBean, BidRequest bidRequest, DSPInfo dspInfo, String content){
        this.sspBean = sspBean;
        this.dspInfo = dspInfo;
        this.content = content;
        this.bidRequest = bidRequest;
    }

    public DSPResponse call() throws Exception  {
        int code;
        DSPResponse response = null;
        JSONObject responseAsJSON = null;
        try{
            if(null != dspInfo.getQps() && dspInfo.getQps()>0)
                sspBean.getQpsCounter().increase(dspInfo.getUserId(), dspInfo.getQps());
            logger.debug("Calling dsp..:- "+ dspInfo.getPingURL());
            response = sspBean.getDspService().dspBid(dspInfo, content);
            logger.debug("Dsp .:- "+ dspInfo.getPingURL()+ " response:- "+ response.getResponse());
            sspBean.getQpsCounter().decrease(dspInfo.getUserId());
            code = response.getCode();
            if(response.getCode() == HttpStatus.OK.value() && !"".equals(response.getResponse())){
                try{
                    OpenRtb.BidResponse.Builder responseBuilder = this.sspBean.getRtbGenerator().getBidResponse(response.getResponse());
                    if(this.sspBean.getRtbGenerator().isValid(this.bidRequest, responseBuilder)){
                        BidData bidData = new BidData();
                        bidData.setAdm(responseBuilder.getSeatbid(0).getBid(0).getAdm());
                        bidData.setNurl(responseBuilder.getSeatbid(0).getBid(0).getNurl());
                        bidData.setAuctionId(responseBuilder.getId());
                        bidData.setAuctionBidId(responseBuilder.getBidid());
                        bidData.setAuctionPrice(responseBuilder.getSeatbid(0).getBid(0).getPrice());
                        bidData.setAuctionImpId(responseBuilder.getSeatbid(0).getBid(0).getImpid());
                        bidData.setAuctionSeatId(responseBuilder.getSeatbid(0).getSeat());
                        bidData.setAuctionAdId(responseBuilder.getSeatbid(0).getBid(0).getAdid());
                        bidData.setAuctionCurrency(responseBuilder.getCur());
                        response.setBidData(bidData);
                        return  response;
                    }else{
                        response.setCode(417);
                    }
                }catch (IOException e){
                    logger.error("Unable to parse DSP response:- URL"+ dspInfo.getPingURL()+" code "+ response.getCode()+" Message:- "+ response.getResponse()+" "+e.getMessage());
                    response.setCode(417);
                }
            }else{
                logger.debug("DSP Response status:- URL"+ dspInfo.getPingURL()+" code "+ response.getCode()+" Message:- "+ response.getResponse());
            }
        }catch (QPSLimitOverFlowException ex){
           logger.error("DSP QPS limit exceeded. "+ dspInfo.getId() + dspInfo.getPingURL());
           code = ex.getCode();
        }catch (SSPURLException ex){
            logger.error("DSP ping url has issue URL:- "+ dspInfo.getPingURL()+ " Issue:- "+ ex.getMessage());
            sspBean.getQpsCounter().decrease(dspInfo.getUserId());
            code = ex.getCode();
        } catch (Exception ex){
            logger.error(ex.toString());
            code  = 500;
        }
        if(null == response){
            response = new DSPResponse();
            response.setCode(code);
            response.setDspInfo(dspInfo);
        }
        JSONParser jsonParser = new JSONParser();
        if(response.getCode()==200){
            responseAsJSON = (JSONObject) jsonParser.parse(response.getResponse());
        }else{
            responseAsJSON = new JSONObject();
        }
        responseAsJSON.put("error_code", response.getCode());
        response.setResponseAsJSON(responseAsJSON);
        return response;
    }
}
