package com.ssp.api.dto;

import com.ssp.api.entity.jpa.DSPDetail;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/19/17
 * Time: 3:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class DSPResponse {

    private Integer code;
    private DSPDetail dspDetail;
    private String response;
    private Double bidValue;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public DSPDetail getDspDetail() {
        return dspDetail;
    }

    public void setDspDetail(DSPDetail dspDetail) {
        this.dspDetail = dspDetail;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Double getBidValue() {
        return bidValue;
    }

    public void setBidValue(Double bidValue) {
        this.bidValue = bidValue;
    }
}
