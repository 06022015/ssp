package com.ssp.web.manager;

import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.ssp.api.Constant;
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
    public void shutDown() {
        this.mongo.close();
    }

    @PostConstruct
    public void createCollection() {
        /*DBCollection bidRequestCollection = this.mongoTemplate.getCollection(Constant.MONGO_USER_BID_REQUEST_COLLECTION);
        if (bidRequestCollection.count() <= 0)
            this.mongoTemplate.createCollection(Constant.MONGO_USER_BID_REQUEST_COLLECTION);
        DBCollection winningBidCollection = this.mongoTemplate.getCollection(Constant.MONGO_USER_BID_REQUEST_COLLECTION);
        if (winningBidCollection.count() <= 0)
            this.mongoTemplate.createCollection(Constant.MONGO_WIN_BID_RESPONSE_COLLECTION);*/
    }
}
