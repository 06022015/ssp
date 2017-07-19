package com.ssp.api.entity.jpa;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/19/17
 * Time: 3:01 PM
 * To change this template use File | Settings | File Templates.
 */
@MappedSuperclass
public abstract class BaseEntity {

    private static final long serialVersionUID = 1326887243102331826L;

    private Long id;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(unique = true, nullable = false, updatable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
