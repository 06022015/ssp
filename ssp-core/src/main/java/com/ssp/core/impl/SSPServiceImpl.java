package com.ssp.core.impl;

import com.ssp.api.dto.PublisherRequest;
import com.ssp.api.dto.PublisherResponse;
import com.ssp.api.service.SSPService;
import com.ssp.core.util.SSPBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/18/17
 * Time: 11:16 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class SSPServiceImpl implements SSPService{

    @Autowired
    private SSPBean sspBean;

    public PublisherResponse process(PublisherRequest request){

        return null;
    }

}
