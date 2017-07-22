package com.ssp.api.entity.jpa;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/18/17
 * Time: 12:55 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "dsp")
@Cache(usage= CacheConcurrencyStrategy.READ_ONLY, region="dsp")
public class DSPDetail extends BaseEntity{

    private String name;
    private Integer qps;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Integer getQps() {
        return qps;
    }

    public void setQps(Integer qps) {
        this.qps = qps;
    }
}
