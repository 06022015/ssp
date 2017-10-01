package com.ssp.api.entity.jpa;


/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/18/17
 * Time: 12:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class DSPInfo{

    private Long id;
    private Long  userId;
    private String pingURL;
    private Integer qps;
    private Integer maxResponseTime;
    private String requestFormat;
    private boolean compressRequest = false;

    public DSPInfo(Integer userId, String pingURL, Integer qps, String requestFormat, int compressRequest) {
        this.userId = null!= userId?userId.longValue():null;
        this.pingURL = pingURL;
        this.qps = qps;
        this.requestFormat = requestFormat;
        this.compressRequest = compressRequest>0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPingURL() {
        return pingURL;
    }

    public void setPingURL(String pingURL) {
        this.pingURL = pingURL;
    }

    public Integer getQps() {
        return qps;
    }

    public void setQps(Integer qps) {
        this.qps = qps;
    }

    public Integer getMaxResponseTime() {
        return maxResponseTime;
    }

    public void setMaxResponseTime(Integer maxResponseTime) {
        this.maxResponseTime = maxResponseTime;
    }

    public String getRequestFormat() {
        return requestFormat;
    }

    public void setRequestFormat(String requestFormat) {
        this.requestFormat = requestFormat;
    }

    public boolean isCompressRequest() {
        return compressRequest;
    }

    public void setCompressRequest(boolean compressRequest) {
        this.compressRequest = compressRequest;
    }
}
