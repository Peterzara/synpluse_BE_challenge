package com.example.techChallenge.account;

import com.example.techChallenge.transaction.Transaction;

import java.util.List;

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

    public String getCid() {
        return cid;
    }

    public String getIBAN() {
        return IBAN;
    }

    public String getCurrency() {
        return currency;
    }

    public List<Transaction> getTransactionList() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

}
