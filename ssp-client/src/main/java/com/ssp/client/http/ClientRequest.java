package com.ssp.client.http;

import org.codehaus.jettison.json.JSONObject;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/18/17
 * Time: 9:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClientRequest {

    private JSONObject content;
    private HttpConnectionProperty property;

    public ClientRequest(String url, ClientMethod method) {
        this.property = new HttpConnectionProperty(url, method.name());
    }

    public JSONObject getContent() {
        return content;
    }

    public void setContent(JSONObject content) {
        this.content = content;
    }

    public HttpConnectionProperty getProperty() {
        return property;
    }

    public void setProperty(HttpConnectionProperty property) {
        this.property = property;
    }
}
