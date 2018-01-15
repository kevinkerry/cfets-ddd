package com.val.example.axon3framework.entities;

import javax.persistence.Entity;
import javax.persistence.Id;



@Entity
public class AccountBalance {
    @Id
    private String id;

    private int balance;

    public AccountBalance(String id, int balance) {
        this.id = id;
        this.balance = balance;
    }
}
