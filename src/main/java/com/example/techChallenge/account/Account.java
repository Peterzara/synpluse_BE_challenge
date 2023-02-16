package com.example.techChallenge.account;

import com.example.techChallenge.transaction.Transaction;
import lombok.Data;

import java.util.List;

@Data
public class Account {
    final private String cid;
    final private String IBAN;
    final private String currency;
    private List<Transaction> transactions;

    public Account(String cid, String IBAN, String currency) {
        this.cid = cid;
        this.IBAN = IBAN;
        this.currency = currency;
    }
}
