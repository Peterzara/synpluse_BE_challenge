package com.example.techChallenge.transaction;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.util.Date;

@JsonDeserialize
@Data
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
