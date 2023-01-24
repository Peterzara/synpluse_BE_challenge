package com.example.techChallenge.transaction;

import java.util.List;

public class TransactionResponse {

    private List<TransactionData> transactions;
    private double totalCredit;
    private double totalDebit;
    private int currentPage;
    private int totalPages;

    public TransactionResponse(List<TransactionData> transactions, double totalCredit, double totalDebit, int currentPage, int totalPages) {
        this.transactions = transactions;
        this.totalCredit = totalCredit;
        this.totalDebit = totalDebit;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
    }

    public List<TransactionData> getTransactions() {
        return transactions;
    }

    public double getTotalCredit() {
        return totalCredit;
    }

    public double getTotalDebit() {
        return totalDebit;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public static class TransactionData {
        private String id;
        private double credit;
        private double debit;
        private double exchangeRate;

        public TransactionData(String id, double credit, double debit, double exchangeRate) {
            this.id = id;
            this.credit = credit;
            this.debit = debit;
            this.exchangeRate = exchangeRate;
        }

        public String getId() {
            return id;
        }

        public double getCredit() {
            return credit;
        }

        public double getDebit() {
            return debit;
        }

        public double getExchangeRate() {
            return exchangeRate;
        }
    }
}

