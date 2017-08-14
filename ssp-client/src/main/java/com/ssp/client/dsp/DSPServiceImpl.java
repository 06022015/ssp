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

import java.util.HashMap;
import java.util.Map;

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

    private static Map<String,String> res = new HashMap<String,String>();

    public DSPServiceImpl() {
        res.put("http://182.72.85.2/djaxtesting/payal_test/rtb/dreamSSP.php?ex=67","{  \n" +
                "   \"id\":\"22eae5d8-8ce9-49dd-858c-a0b34554203d\",\n" +
                "   \"seatbid\":[  \n" +
                "      {  \n" +
                "         \"bid\":[  \n" +
                "            {  \n" +
                "               \"id\":\"a246fed820183778d957b960a57ec925\",\n" +
                "               \"impid\":\"1\",\n" +
                "               \"price\":\"1.0000\",\n" +
                "               \"adid\":\"1401\",\n" +
                "               \"nurl\":\"http:\\/\\/182.72.85.2\\/djaxtesting\\/payal_test\\/rtb\\/win_notice.php?exchange=67&dtype=1&auctionId=${AUCTION_ID}&bidid=${AUCTION_BID_ID}&price=${AUCTION_PRICE}&impid=${AUCTION_IMP_ID}&seatid=${AUCTION_SEAT_ID}&adid=${AUCTION_AD_ID}&cur=${AUCTION_CURRENCY}\",\n" +
                "               \"adm\":\"<a href='http:\\/\\/182.72.85.2\\/djaxtesting\\/payal_test\\/rtb\\/\\/click.php?ZXhjaGFuZ2U9NjcmdXJsPSZiaWRpZD0yMmVhZTVkOC04Y2U5LTQ5ZGQtODU4Yy1hMGIzNDU1NDIwM2QmZHR5cGU9MSZhZGlkPTE0MDE=' target='_blank'><img src='http:\\/\\/182.72.85.2\\/djaxtesting\\/payal_test\\/ads\\/www\\/images\\/88b2675fb7fdfe48f6002c9e1959c680.jpeg' alt='88b2675fb7fdfe48f6002c9e1959c680.jpeg' \\/><\\/a><img src='http:\\/\\/182.72.85.2\\/djaxtesting\\/payal_test\\/rtb\\/\\/impression.php?ZXhjaGFuZ2U9NjcmYmlkaWQ9MjJlYWU1ZDgtOGNlOS00OWRkLTg1OGMtYTBiMzQ1NTQyMDNkJmR0eXBlPTEmYWRpZD0xNDAx' alt='' width='0' height='0' \\/>\",\n" +
                "               \"adomain\":[  \n" +
                "                  \"dreamajax.com\"\n" +
                "               ],\n" +
                "               \"iurl\":\"http:\\/\\/182.72.85.2\\/djaxtesting\\/payal_test\\/ads\\/www\\/images\\/88b2675fb7fdfe48f6002c9e1959c680.jpeg\",\n" +
                "               \"cid\":\"931\",\n" +
                "               \"crid\":\"1401\",\n" +
                "               \"attr\":[  \n" +
                "\n" +
                "               ],\n" +
                "               \"ext\":null,\n" +
                "               \"cat\":[  \n" +
                "                                ]\n" +
                "            }\n" +
                "         ],\n" +
                "         \"seat\":\"0\",\n" +
                "         \"group\":\"0\"\n" +
                "      }\n" +
                "   ],\n" +
                "   \"bidid\":\"22eae5d8-8ce9-49dd-858c-a0b34554203d\",\n" +
                "   \"customdata\":null,\n" +
                "   \"cur\":\"USD\",\n" +
                "   \"ext\":null\n" +
                "}");
        res.put("http://182.72.85.2:8010/development/yayimla_dev/dsp/dreamSSP.php?ex=186","{  \n" +
                "   \"id\":\"22eae5d8-8ce9-49dd-858c-a0b34554203d\",\n" +
                "   \"seatbid\":[  \n" +
                "      {  \n" +
                "         \"bid\":[  \n" +
                "            {  \n" +
                "               \"id\":\"8f62ce9d3878e7fa7e77be0e50e05680\",\n" +
                "               \"impid\":\"1\",\n" +
                "               \"price\":\"1.000000\",\n" +
                "               \"adid\":\"155\",\n" +
                "               \"nurl\":\"http:\\/\\/182.72.85.2:8010\\/development\\/yayimla_dev\\/dsp\\/\\/win_notice.php?exchange=186&dtype=1&auctionId=${AUCTION_ID}&bidid=${AUCTION_BID_ID}&price=${AUCTION_PRICE}&impid=${AUCTION_IMP_ID}&seatid=${AUCTION_SEAT_ID}&adid=${AUCTION_AD_ID}&cur=${AUCTION_CURRENCY}\",\n" +
                "               \"adm\":\"<a href='http:\\/\\/182.72.85.2:8010\\/development\\/yayimla_dev\\/dsp\\/\\/click.php?ZXhjaGFuZ2U9MTg2JnVybD1odHRwOi8vd3d3Lmdvb2dsZS5jb20mYmlkaWQ9MjJlYWU1ZDgtOGNlOS00OWRkLTg1OGMtYTBiMzQ1NTQyMDNkJmR0eXBlPTEmYWRpZD0xNTU=' target='_blank'><img src='http:\\/\\/cdn.maxxrtb.com\\/3e34995b3e0f4da4f3e9a29643406362.jpeg' alt='dreamSSP_mobile_300_250' \\/><\\/a><img src='http:\\/\\/182.72.85.2:8010\\/development\\/yayimla_dev\\/dsp\\/\\/impression.php?ZXhjaGFuZ2U9MTg2JmJpZGlkPTIyZWFlNWQ4LThjZTktNDlkZC04NThjLWEwYjM0NTU0MjAzZCZkdHlwZT0xJmFkaWQ9MTU1' alt='' width='0' height='0' \\/>\",\n" +
                "               \"adomain\":[  \n" +
                "                  \"http:\\/\\/182.72.85.2:8010\\/development\\/yayimla_dev\\/dsp\\/\"\n" +
                "               ],\n" +
                "               \"iurl\":\"http:\\/\\/cdn.maxxrtb.com\\/3e34995b3e0f4da4f3e9a29643406362.jpeg\",\n" +
                "               \"cid\":\"100\",\n" +
                "               \"crid\":\"155\",\n" +
                "               \"attr\":[  \n" +
                "\n" +
                "               ],\n" +
                "               \"ext\":null,\n" +
                "               \"cat\":[  \n" +
                "               ]\n" +
                "            }\n" +
                "         ],\n" +
                "         \"seat\":\"0\",\n" +
                "         \"group\":\"0\"\n" +
                "      }\n" +
                "   ],\n" +
                "   \"bidid\":\"22eae5d8-8ce9-49dd-858c-a0b34554203d\",\n" +
                "   \"customdata\":null,\n" +
                "   \"cur\":\"USD\",\n" +
                "   \"ext\":null\n" +
                "}");

    }

    @Autowired
    private SSPHttpClient client;

    public DSPResponse dspBid(DSPInfo dspInfo, String content) throws SSPURLException {
        ClientRequest request = new ClientRequest(dspInfo.getPingURL(), ClientMethod.POST);
        request.setContent(content);
        request.setCompressed(dspInfo.isCompressRequest());
        request.put(ClientRequest.CONNECTION_TIMEOUT_NAME, 100);
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
