package com.ssp.api.dto;

import org.codehaus.jettison.json.JSONObject;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/18/17
 * Time: 11:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class PublisherResponse {

    private String response;

    public PublisherResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public JSONObject toJSON(){
        JSONObject json = new JSONObject();
        return json;
    }
}
