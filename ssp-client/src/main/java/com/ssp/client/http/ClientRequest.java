package com.ssp.client.http;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/18/17
 * Time: 9:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClientRequest {

    public final static String CONTENT_TYPE = "application/json";
    public final static String ACCEPT_TYPE = "application/json";
    public final static String ACCEPT_LANGUAGE = "en-US,en;q=0.5";


    public final static String CONTENT_TYPE_NAME = "Content-Type";
    public final static String ACCEPT_TYPE_NAME = "Accept";
    public final static String ACCEPT_LANGUAGE_NAME = "Accept-Language";
    public final static String CONNECTION_TIMEOUT_NAME = "connectionTimeout";
    public final static String READ_TIMEOUT_NAME = "readTimeout";
    public final static String USE_CACHE_NAME = "userCache";

    private final String url;
    private String method;
    private String content;
    private boolean compressed = false;
    private Map<String,Object> property;

    private void init(){
        this.property = new HashMap<String,Object>();
        this.property.put(CONTENT_TYPE_NAME, CONTENT_TYPE);
        //this.property.put(ACCEPT_TYPE_NAME, ACCEPT_TYPE);
    }

    public ClientRequest(String url, ClientMethod method) {
        this.url = url;
        this.method = method.name();
        init();
    }

    public Map<String, Object> getProperty() {
        return property;
    }

    public void put(String name, Object value){
        this.property.put(name, value);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public boolean isCompressed() {
        return compressed;
    }

    public void setCompressed(boolean compressed) {
        this.compressed = compressed;
    }

    public String getMethod() {
        return method;
    }
}
