package com.ssp.core.util;

import com.ssp.api.repository.jpa.JPARepository;
import com.ssp.api.repository.mongo.MongoRepository;
import com.ssp.api.service.DSPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/19/17
 * Time: 5:29 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class SSPBean {

    @Autowired
    private JPARepository jpaRepository;

    @Autowired
    private MongoRepository mongoRepository;

    @Autowired
    private DSPQPSCounter qpsCounter;

    @Autowired
    private DSPService dspService;

    public JPARepository getJpaRepository() {
        return jpaRepository;
    }

    public MongoRepository getMongoRepository() {
        return mongoRepository;
    }

    public DSPQPSCounter getQpsCounter() {
        return qpsCounter;
    }

    public DSPService getDspService() {
        return dspService;
    }
}
