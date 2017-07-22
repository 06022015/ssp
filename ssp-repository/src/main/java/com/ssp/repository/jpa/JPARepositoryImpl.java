package com.ssp.repository.jpa;

import com.ssp.api.entity.jpa.DSPDetail;
import com.ssp.api.entity.jpa.PublisherEntity;
import com.ssp.api.repository.jpa.JPARepository;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/19/17
 * Time: 12:05 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository("jpaRepository")
@Transactional
public class JPARepositoryImpl implements JPARepository {

    private static  List<DSPDetail> dspDetails = new ArrayList<DSPDetail>();

    public JPARepositoryImpl(){
        for(int i=0;i< 10; i++){
            DSPDetail dspDetail = new DSPDetail();
            dspDetail.setId((long)i);
            dspDetail.setName("Ashif "+ i);
            dspDetail.setQps((i+1)*200);
            dspDetails.add(dspDetail);
        }
    }


    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<DSPDetail> getAllDSP() {
        return dspDetails;
    }

    @Override
    public List<PublisherEntity> getAllPublisher() {
        List<PublisherEntity> publishers = new ArrayList<PublisherEntity>();
        for(int i=0;i< 10; i++){
            PublisherEntity dspDetail = new PublisherEntity();
            dspDetail.setId((long) i);
            dspDetail.setName("Ashif " + i);
            publishers.add(dspDetail);
        }
        return publishers;
    }
}
