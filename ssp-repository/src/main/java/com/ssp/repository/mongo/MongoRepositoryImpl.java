package com.ssp.repository.mongo;


import com.ssp.api.repository.mongo.MongoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.Calendar;

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

    private static String RTB_REQUEST_COLLECTION = "request_";
    private static String DSP_RESPONSE_COLLECTION = "response_";

    @Autowired
    private MongoTemplate mongoTemplate;

    public void saveRTBJSON(String bidRequest) {
        /*DBObject dbObject = (DBObject) JSON.parse(bidRequest);*/
        /*DBCollection dbCollection = mongoTemplate.getCollection(Constant.MONGO_USER_BID_REQUEST_COLLECTION);
        dbCollection.insert(dbObject);*/
        mongoTemplate.insert(bidRequest,getCollectionName(RTB_REQUEST_COLLECTION));
    }

    public void saveDSPResponse(String dspResponse) {
        /*DBObject dbObject = (DBObject) JSON.parse(dspResponse);*/
        mongoTemplate.insert(dspResponse, getCollectionName(DSP_RESPONSE_COLLECTION));
    }

    private String getCollectionName(String prefix){
        Calendar calendar = Calendar.getInstance();
        StringBuilder sb  = new StringBuilder(prefix);
        sb.append(getAsDoubleDigit(Calendar.DATE));
        sb.append(getAsDoubleDigit(calendar.get(Calendar.MONTH)));
        sb.append(calendar.get(Calendar.YEAR));
        sb.append(getAsDoubleDigit(calendar.get(Calendar.HOUR_OF_DAY)));
        return sb.toString();
    }

    private String getAsDoubleDigit(int number){
        if(number/10==0)
            return "0"+number;
        else
            return number+"";

    }
}
