package com.ssp.repository.mongo;


import com.ssp.api.repository.mongo.MongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/19/17
 * Time: 12:05 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository("mongoRepository")
@Transactional
public class MongoRepositoryImpl implements MongoRepository {


    @Autowired
    private MongoTemplate mongoTemplate;


}
