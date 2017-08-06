package com.ssp.api.service;

import com.ssp.api.dto.DSPResponse;
import com.ssp.api.entity.jpa.DSPInfo;
import com.ssp.api.exception.SSPURLException;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/19/17
 * Time: 12:16 AM
 * To change this template use File | Settings | File Templates.
 */
public interface DSPService {


    DSPResponse dspBid(DSPInfo dspInfo, String content)throws SSPURLException;

    Integer notifyDSP(String notifyURL);

}
