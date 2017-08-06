package com.ssp.api.service;

import com.ssp.api.dto.BidData;
import com.ssp.api.exception.SSPException;

import java.io.IOException;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/19/17
 * Time: 12:16 AM
 * To change this template use File | Settings | File Templates.
 */
public interface SSPService {

    BidData processRequest(Map<String,String> parameter) throws SSPException, IOException;

}
