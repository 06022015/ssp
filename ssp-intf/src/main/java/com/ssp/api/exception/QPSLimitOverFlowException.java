package com.ssp.api.exception;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/19/17
 * Time: 12:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class QPSLimitOverFlowException extends SSPException{


    public QPSLimitOverFlowException(int code, String message) {
        super(code, message);
    }

    public QPSLimitOverFlowException(String message, Throwable cause, int code) {
        super(message, cause, code);
    }

    public QPSLimitOverFlowException(Throwable cause, int code) {
        super(cause, code);
    }

    public QPSLimitOverFlowException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, int code) {
        super(message, cause, enableSuppression, writableStackTrace, code);
    }

    public QPSLimitOverFlowException(Throwable cause, String message) {
        super(cause, message);
    }
}
