package com.ssp.repository.jpa;

import com.ssp.api.repository.jpa.JPARepository;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private SessionFactory sessionFactory;

}
