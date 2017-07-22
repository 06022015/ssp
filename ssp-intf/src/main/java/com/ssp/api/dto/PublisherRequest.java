package com.ssp.api.dto;


import org.codehaus.jettison.json.JSONObject;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/18/17
 * Time: 11:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class PublisherRequest {

    private String publisherId;

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public static PublisherRequest parse(JSONObject json){
        PublisherRequest request = new PublisherRequest();

        return request;
    }

}
