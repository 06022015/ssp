package com.ssp.api.repository.mongo;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/19/17
 * Time: 12:05 AM
 * To change this template use File | Settings | File Templates.
 */
public interface MongoRepository {

     void saveRTBJSON(String bidRequest);

    void saveDSPResponse(String winningBid);

}
