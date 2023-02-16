package com.example.techChallenge.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class TransactionResponse {

    private List<TransactionData> transactions;
    private double totalCredit;
    private double totalDebit;
    private int currentPage;
    private int totalPages;
    @Data
    @AllArgsConstructor
    public static class TransactionData {
        private String id;
        private double credit;
        private double debit;
        private double exchangeRate;
    }
}

