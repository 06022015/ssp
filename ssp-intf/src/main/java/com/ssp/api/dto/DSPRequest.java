package com.ssp.api.dto;

import com.ssp.api.entity.jpa.DSPDetail;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/19/17
 * Time: 3:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class DSPRequest {

    private DSPDetail dspDetail;
    private String content;

    public DSPDetail getDspDetail() {
        return dspDetail;
    }

    public void setDspDetail(DSPDetail dspDetail) {
        this.dspDetail = dspDetail;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
