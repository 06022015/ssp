package com.ssp.api.service;

import com.ssp.api.dto.DSPResponse;
import com.ssp.api.entity.jpa.DSPDetail;
import org.codehaus.jettison.json.JSONObject;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/19/17
 * Time: 12:16 AM
 * To change this template use File | Settings | File Templates.
 */
public interface DSPService {


    DSPResponse dspBid(DSPDetail dspDetail, JSONObject content);

     void notifyDSP(DSPDetail dspDetail, JSONObject content);

}
