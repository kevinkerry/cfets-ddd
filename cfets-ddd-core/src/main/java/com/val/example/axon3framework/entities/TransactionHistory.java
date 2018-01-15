package com.val.example.axon3framework.entities;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity

public class TransactionHistory {

    @Id
    @GeneratedValue
    private long id;

    private String accountId;

    private int amount;

    private String transactionId;

    public TransactionHistory(String accountId, int amount, String transactionId) throws TransactionHistoryCreateException {
        if(StringUtils.isBlank(accountId)||StringUtils.isBlank(transactionId)){
            throw new TransactionHistoryCreateException();
        }
        this.accountId = accountId;
        this.amount = amount;
        this.transactionId = transactionId;
    }

   public  class TransactionHistoryCreateException extends Exception{



    }

}
