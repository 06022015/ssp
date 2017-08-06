package com.ssp.client.http;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/18/17
 * Time: 9:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class HttpConnectionProperty {
    
    private final static String CONTENT_TYPE = "application/json";
    private final static String ACCEPT_TYPE = "application/json";

    private final String url;
    private int connectTimeOut = 100;
    private int readTimeOut = 200;
    private String method;
    private boolean useCache = false;
    private String contentType ;
    private String acceptType ;

    public HttpConnectionProperty(String url, String method) {
        this.url = url;
        this.method = method;
        this.contentType = CONTENT_TYPE;
        this.acceptType = ACCEPT_TYPE;
    }

    public String getUrl() {
        return url;
    }

    public int getConnectTimeOut() {
        return connectTimeOut;
    }

    public void setConnectTimeOut(int connectTimeOut) {
        this.connectTimeOut = connectTimeOut;
    }

    public int getReadTimeOut() {
        return readTimeOut;
    }

    public void setReadTimeOut(int readTimeOut) {
        this.readTimeOut = readTimeOut;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public boolean isUseCache() {
        return useCache;
    }

    public void setUseCache(boolean useCache) {
        this.useCache = useCache;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getAcceptType() {
        return acceptType;
    }

    public void setAcceptType(String acceptType) {
        this.acceptType = acceptType;
    }
}
