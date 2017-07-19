package com.ssp.web.manager;

import com.mongodb.Mongo;
import com.ssp.api.entity.mongo.RTBDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/18/17
 * Time: 12:50 AM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class MongoDBManager {

    @Autowired
    private Mongo mongo;

    @Autowired
    private MongoTemplate mongoTemplate;

    @PreDestroy
    public void shutDown(){
        this.mongo.close();
    }

    @PostConstruct
    public void insertRTBRe(){
        /*for(int i=0;i< 10; i++){
            RTBDetails rtbDetails = new RTBDetails();
            rtbDetails.setId((long) i);
            rtbDetails.setName("Ashif Qureshi"+i);
            mongoTemplate.save(rtbDetails);
        }*/

        System.out.println("RTB Request count:- "+this.mongoTemplate.findAll(RTBDetails.class).size());


    }


}
