package com.ssp.client.http;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/18/17
 * Time: 9:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClientResponse {
    
    private Integer code;
    private String response;

    public ClientResponse(Integer code, String response) {
        this.code = code;
        this.response = response;
    }

    public Integer getCode() {
        return code;
    }

    public String getResponse() {
        return response;
    }
}
