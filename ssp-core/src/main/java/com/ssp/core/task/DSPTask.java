package com.ssp.core.task;

import com.google.openrtb.OpenRtb;
import com.google.openrtb.OpenRtb.BidRequest;
import com.ssp.api.dto.BidData;
import com.ssp.api.dto.DSPResponse;
import com.ssp.api.entity.jpa.DSPInfo;
import com.ssp.api.exception.QPSLimitOverFlowException;
import com.ssp.api.exception.SSPURLException;
import com.ssp.core.util.SSPBean;
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
        try{
            if(null != dspInfo.getQps() && dspInfo.getQps()>0)
                sspBean.getQpsCounter().increase(dspInfo.getUserId(), dspInfo.getQps());
            logger.info("Calling dsp..:- "+ dspInfo.getPingURL());
            DSPResponse response = sspBean.getDspService().dspBid(dspInfo, content);
            logger.info("Dsp .:- "+ dspInfo.getPingURL()+ " response:- "+ response.getResponse());
            sspBean.getQpsCounter().decrease(dspInfo.getUserId());
            if(response.getCode() == HttpStatus.OK.value() && !"".equals(response.getResponse())){
                try{
                    OpenRtb.BidResponse.Builder responseBuilder = this.sspBean.getRtbGenerator().getBidResponse(response.getResponse());
                    if(this.sspBean.getRtbGenerator().isValid(this.bidRequest, responseBuilder)){
                        logger.info("saving  winning bid "+ dspInfo.getPingURL());
                        this.sspBean.getMongoRepository().saveWinningBid(response.getResponse());
                        logger.info("saved  winning bid"+ dspInfo.getPingURL());
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
                    }
                }catch (IOException e){
                    logger.info("Unable to parse DSP response:- URL"+ dspInfo.getPingURL()+" code "+ response.getCode()+" Message:- "+ response.getResponse()+" "+e.getMessage());
                }
            }else{
                logger.info("DSP Response status:- URL"+ dspInfo.getPingURL()+" code "+ response.getCode()+" Message:- "+ response.getResponse());
            }
        }catch (QPSLimitOverFlowException ex){
           logger.info("DSP QPS limit exceeded. "+ dspInfo.getId() + dspInfo.getPingURL());
        }catch (SSPURLException ex){
            logger.info("DSP ping url has issue URL:- "+ dspInfo.getPingURL()+ " Issue:- "+ ex.getMessage());
            sspBean.getQpsCounter().decrease(dspInfo.getUserId());
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
}
