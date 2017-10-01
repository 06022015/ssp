package com.ssp.client.dsp;

import com.ssp.api.dto.DSPResponse;
import com.ssp.api.entity.jpa.DSPInfo;
import com.ssp.api.exception.SSPURLException;
import com.ssp.api.service.DSPService;
import com.ssp.client.http.ClientMethod;
import com.ssp.client.http.ClientRequest;
import com.ssp.client.http.ClientResponse;
import com.ssp.client.http.SSPHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/19/17
 * Time: 12:16 AM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class DSPServiceImpl implements DSPService {

    private static Logger logger = LoggerFactory.getLogger(DSPServiceImpl.class);

    @Autowired
    private Properties properties;

    @Autowired
    private SSPHttpClient client;

    public DSPResponse dspBid(DSPInfo dspInfo, String content) throws SSPURLException {
        ClientRequest request = new ClientRequest(dspInfo.getPingURL(), ClientMethod.POST);
        request.setContent(content);
        request.setCompressed(dspInfo.isCompressRequest());
        request.put(ClientRequest.CONNECTION_TIMEOUT_NAME, Integer.parseInt(properties.getProperty("dsp.connection.time.max")));
        if (dspInfo.getMaxResponseTime() > 0)
            request.put(ClientRequest.READ_TIMEOUT_NAME, dspInfo.getMaxResponseTime());
        ClientResponse response = client.post(request);
        DSPResponse dspResponse = new DSPResponse();
        dspResponse.setCode(response.getCode());
        dspResponse.setDspInfo(dspInfo);
        dspResponse.setResponse(response.getResponse());
        return dspResponse;
    }

    public Integer notifyDSP(String notifyURL) {
        ClientResponse response = client.get(notifyURL);
        return response.getCode();
    }
}
