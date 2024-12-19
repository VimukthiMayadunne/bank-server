package org.dev.shared.bankserver.pojo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;


@Entity
public class Customer {
    @Id
    private Long cusId;
    private String cusName;
    private String email;


    public Customer() {

    }

    public Customer(Long cusId, String cusName, String email) {
        this.cusId = cusId;
        this.cusName = cusName;
        this.email = email;
    }

    public Long getCusId() {
        return cusId;
    }

    public void setCusId(Long cusId) {
        this.cusId = cusId;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
