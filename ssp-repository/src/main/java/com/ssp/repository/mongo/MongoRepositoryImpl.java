package com.ssp.repository.mongo;


import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.ssp.api.Constant;
import com.ssp.api.repository.mongo.MongoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/19/17
 * Time: 12:05 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository("mongoRepository")
public class MongoRepositoryImpl implements MongoRepository {

    private static Logger logger = LoggerFactory.getLogger(MongoRepositoryImpl.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    public void saveRTBJSON(String bidRequest) {
        DBObject dbObject = (DBObject) JSON.parse(bidRequest);
        DBCollection dbCollection = mongoTemplate.getCollection(Constant.MONGO_USER_BID_REQUEST_COLLECTION);
        dbCollection.insert(dbObject);
    }

    public void saveWinningBid(String winningBid) {
        DBObject dbObject = (DBObject) JSON.parse(winningBid);
        DBCollection dbCollection = mongoTemplate.getCollection(Constant.MONGO_WIN_BID_RESPONSE_COLLECTION);
        dbCollection.insert(dbObject);
    }
}
