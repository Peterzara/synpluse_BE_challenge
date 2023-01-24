package com.example.techChallenge.customer;

import com.example.techChallenge.account.Account;

import java.util.List;

public class Customer {
    final private int cid;
    private List<Account> accounts;

    public Customer(int cid) {
        this.cid = cid;
    }

    public int getCid() {
        return cid;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
}
