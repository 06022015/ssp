package com.ssp.api.exception;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/18/17
 * Time: 9:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class HttpConnectionException extends SSPException{


    public HttpConnectionException(int code, String message) {
        super(code, message);
    }
    
    public HttpConnectionException(String message, Throwable cause, int code) {
        super(message, cause, code);
    }

    public HttpConnectionException(Throwable cause, int code) {
        super(cause, code);
    }

    public HttpConnectionException(Throwable cause, String message) {
        super(cause, message);
    }

    public HttpConnectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, int code) {
        super(message, cause, enableSuppression, writableStackTrace, code);
    }
}
