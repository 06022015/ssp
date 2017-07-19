package com.ssp.api.entity.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/18/17
 * Time: 12:59 AM
 * To change this template use File | Settings | File Templates.
 */
@Document(collection = "rtb_details")
public class RTBDetails implements Serializable {

    private static final long serialVersionUID = 1326887243102331826L;

    private Long id;
    private String name;

    public RTBDetails() {
    }

    public RTBDetails(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "RTBDetails{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
