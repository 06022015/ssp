package com.ssp.api.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/18/17
 * Time: 9:04 PM
 * To change this template use File | Settings | File Templates.
 */
public  class SSPException extends RuntimeException{

    private static Logger logger = LoggerFactory.getLogger(SSPException.class);
    
    private int code;

    public SSPException(int code, String message) {
        super(message);
        this.code = code;
        logger.error(message);
    }

    public SSPException(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = code;
        logger.error(message);
    }

    public SSPException(Throwable cause, int code) {
        super(cause);
        this.code = code;
        logger.error(cause.getMessage());
    }

    public SSPException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, int code) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
        logger.error(message);
    }

    public SSPException(Throwable cause, String message) {
        super(message, cause);
        logger.error(message);
    }

    public int getCode() {
        return code;
    }


}
