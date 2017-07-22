package com.ssp.api.repository.jpa;

import com.ssp.api.entity.jpa.DSPDetail;
import com.ssp.api.entity.jpa.PublisherEntity;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/19/17
 * Time: 12:05 AM
 * To change this template use File | Settings | File Templates.
 */
public interface JPARepository {

    List<DSPDetail> getAllDSP();


    List<PublisherEntity> getAllPublisher();

}
