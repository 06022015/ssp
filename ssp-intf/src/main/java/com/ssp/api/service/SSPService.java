package com.ssp.api.service;

import com.ssp.api.dto.PublisherRequest;
import com.ssp.api.dto.PublisherResponse;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/19/17
 * Time: 12:16 AM
 * To change this template use File | Settings | File Templates.
 */
public interface SSPService {

    PublisherResponse processRequest(PublisherRequest request);

}
