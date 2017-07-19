package com.ssp.client.dsp;

import com.ssp.api.dto.DSPResponse;
import com.ssp.api.entity.jpa.DSPDetail;
import com.ssp.api.service.DSPService;
import com.ssp.client.http.ClientMethod;
import com.ssp.client.http.ClientRequest;
import com.ssp.client.http.ClientResponse;
import com.ssp.client.http.SSPHttpClient;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/19/17
 * Time: 12:16 AM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class DSPServiceImpl implements DSPService{
    
    private static Logger logger = LoggerFactory.getLogger(DSPServiceImpl.class);

    @Autowired
    private SSPHttpClient client;

    @Override
    public DSPResponse dspBid(DSPDetail dspDetail, JSONObject content) {
        ClientRequest request = new ClientRequest(dspDetail.getName(), ClientMethod.POST);
        request.setContent(content);
        ClientResponse response = client.post(request);
        DSPResponse dspResponse = new DSPResponse();
        dspResponse.setCode(response.getCode());
        dspResponse.setDspDetail(dspDetail);
        dspResponse.setResponse(response.getResponse());
        return dspResponse;
    }

    @Override
    public void notifyDSP(DSPDetail dspDetail, JSONObject content) {
        ClientRequest request = new ClientRequest(dspDetail.getName(), ClientMethod.POST);
        request.setContent(content);
        client.post(request);
    }
}
