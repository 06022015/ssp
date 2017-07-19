package com.ssp.api.exception;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/18/17
 * Time: 9:04 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class SSPException extends RuntimeException{
    
    private int code;

    protected SSPException(int code, String message) {
        super(message);
        this.code = code;
    }

    protected SSPException(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }

    protected SSPException(Throwable cause, int code) {
        super(cause);
        this.code = code;
    }

    protected SSPException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, int code) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }

    public SSPException(Throwable cause, String message) {
        super(message, cause);
    }

    public int getCode() {
        return code;
    }


}
