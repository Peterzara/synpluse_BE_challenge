package com.example.techChallenge.transaction;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Date;

@JsonDeserialize
public class Transaction {

    /**
     * Unique identifier (e.g. 89d3o179-abcd-465b-o9ee-e2d5f6ofEld46)
     * Amount with currency (eg GBP 100-, CHF 75)
     * Account IBAN (eg. CH93-0000-0000-0000-0000-0)
     * Value date (e.g. 01-10-2020)
     * Description (e.g. Online payment CHF)*/

    private String id;
    private String cid;
    private double amount;
    private Date date;
    private String IBAN;
    private String description;
    private String currency;
    private int month;

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
    public String getId() {
        return id;
    }

    public String getCid() {  return cid;  }
    public double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public Date getDate() {
        return date;
    }

    public String getIBAN() {
        return IBAN;
    }

    public String getDescription() {
        return description;
    }

    @JsonCreator
    public Transaction(@JsonProperty("id") String id,
                       @JsonProperty("cid") String cid,
                       @JsonProperty("amount") double amount,
                       @JsonProperty("date") Date date,
                       @JsonProperty("iban") String iban,
                       @JsonProperty("description") String description,
                       @JsonProperty("currency") String currency,
                       @JsonProperty("month") int month)  {
        this.id = id;
        this.cid = cid;
        this.amount = amount;
        this.date = date;
        this.IBAN = iban;
        this.description = description;
        this.currency = currency;
        this.month = month;
    }
}
