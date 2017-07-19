package com.ssp.api.entity.jpa;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/19/17
 * Time: 3:01 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "publisher")
public class PublisherEntity extends BaseEntity{

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
