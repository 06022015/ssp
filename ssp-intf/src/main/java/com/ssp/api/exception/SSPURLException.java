package com.ssp.api.exception;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/18/17
 * Time: 9:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class SSPURLException extends SSPException{


    public SSPURLException(int code, String message) {
        super(code, message);
    }

    public SSPURLException(String message, Throwable cause, int code) {
        super(message, cause, code);
    }

    public SSPURLException(Throwable cause, int code) {
        super(cause, code);
    }

    public SSPURLException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, int code) {
        super(message, cause, enableSuppression, writableStackTrace, code);
    }
}
