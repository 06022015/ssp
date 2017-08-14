package com.ssp.api.dto;

import com.ssp.api.entity.jpa.DSPInfo;
import org.json.simple.JSONObject;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/19/17
 * Time: 3:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class DSPResponse {

    private Integer code;
    private DSPInfo dspInfo;
    private String response;
    private BidData bidData;
    private JSONObject responseAsJSON;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public DSPInfo getDspInfo() {
        return dspInfo;
    }

    public void setDspInfo(DSPInfo dspInfo) {
        this.dspInfo = dspInfo;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public BidData getBidData() {
        return bidData;
    }

    public void setBidData(BidData bidData) {
        this.bidData = bidData;
    }

    public JSONObject getResponseAsJSON() {
        return responseAsJSON;
    }

    public void setResponseAsJSON(JSONObject responseAsJSON) {
        this.responseAsJSON = responseAsJSON;
    }
}
