package com.ssp.core.util;

import com.ssp.api.repository.jpa.JPARepository;
import com.ssp.api.repository.mongo.MongoRepository;
import com.ssp.api.service.DSPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Properties;

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

    @Autowired
    private GeoLocationService locationService;

    @Autowired
    private ThreadPoolTaskExecutor dspExecutor;

    @Autowired
    private ThreadPoolTaskExecutor dspNotifyExecutor;

    @Autowired
    private Properties properties;

    private RTBGenerator rtbGenerator;


    public SSPBean() {
        rtbGenerator = new RTBGenerator();
    }

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

    public GeoLocationService getLocationService() {
        return locationService;
    }

    public ThreadPoolTaskExecutor getDspExecutor() {
        return dspExecutor;
    }

    public ThreadPoolTaskExecutor getDspNotifyExecutor() {
        return dspNotifyExecutor;
    }

    public RTBGenerator getRtbGenerator() {
        return rtbGenerator;
    }

    public Properties getProperties() {
        return properties;
    }
}
