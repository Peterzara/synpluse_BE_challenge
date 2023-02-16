package com.example.techChallenge.customer;

import com.example.techChallenge.exchangeRate.ExchangeRateService;
import com.example.techChallenge.transaction.Transaction;
import com.example.techChallenge.transaction.TransactionResponse;
import com.example.techChallenge.transcationKafkaConfig.KafkaConsumer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/customers/{customerId}/transactions")
@RequiredArgsConstructor
public class CustomerController {
    private final KafkaConsumer kafkaConsumer;
    @Value("${transactions.per.page}")
    private int transactionsPerPage;
    private final ExchangeRateService exchangeRateService;

    /**
     * get trans response by cid, month and page
     * @param customerId
     * @param month
     * @param pageNumber
     * @return
     * @URI /customers/{customerId}/transactions?month={month}&page={pageNumber}
     */
    @GetMapping
    public ResponseEntity<TransactionResponse> getTransactions(
            @PathVariable("customerId") String customerId,
            @RequestParam("month") int month,
            @RequestParam("page") int pageNumber) {

        List<Transaction> transactions = kafkaConsumer.getTransactionsByCustomerIDAndMonth(customerId, month);
        if (transactions == null) {
            return ResponseEntity.notFound().build();
        }
        List<Transaction> paginatedTransactions = null;
        int totalPages = (int) Math.ceil(transactions.size() / (double) transactionsPerPage);
        try {
            int fromIndex = (pageNumber - 1) * transactionsPerPage;
            int toIndex = Math.min(fromIndex + transactionsPerPage, transactions.size());
            paginatedTransactions = transactions.subList(fromIndex, toIndex);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new TransactionResponse(null, -1, -1, pageNumber, totalPages));
        }
        double totalCredit = 0;
        double totalDebit = 0;
        List<TransactionResponse.TransactionData> responseTransactions = new ArrayList<>();
        System.out.println("Total trans: " + paginatedTransactions.size());
        for (Transaction transaction : paginatedTransactions) {
            double exchangeRate = exchangeRateService.getExchangeRate(transaction.getCurrency());
            double credit = transaction.getAmount() * exchangeRate;
            double debit = 0;
            if (transaction.getAmount() < 0) {
                debit = credit;
                credit = 0;
            } else {
                totalCredit += credit;
            }
            responseTransactions.add(new TransactionResponse.TransactionData(transaction.getId(), credit, debit, exchangeRate));
        }
        totalDebit = totalCredit - transactions.stream().mapToDouble(Transaction::getAmount).sum();
        return ResponseEntity.ok(new TransactionResponse(responseTransactions, totalCredit, totalDebit, pageNumber, totalPages));
    }

}
