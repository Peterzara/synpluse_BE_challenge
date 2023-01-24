package com.example.techChallenge.exchangeRate;

import java.util.Date;

public class ExchangeRate {
    private String currency;
    private double rate;
    private Date date;

    public ExchangeRate(String currency, double rate, Date date) {
        this.currency = currency;
        this.rate = rate;
        this.date = date;
    }

    public String getCurrency() {
        return currency;
    }

    public double getRate() {
        return rate;
    }

    public Date getDate() {
        return date;
    }
}
