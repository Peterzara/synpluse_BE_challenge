package com.example.techChallenge.customer;

import com.example.techChallenge.exchangeRate.ExchangeRateService;
import com.example.techChallenge.transaction.Transaction;
import com.example.techChallenge.transaction.TransactionResponse;
import com.example.techChallenge.transcationKafkaConfig.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/customers/{customerId}/transactions")
public class CustomerController {
    @Autowired
    private KafkaConsumer kafkaConsumer;
    @Value("${transactions.per.page}")
    private int transactionsPerPage=100;

    @Autowired
    private ExchangeRateService exchangeRateService;

    public CustomerController(KafkaConsumer kafkaConsumer,
                              @Value("${transactions.per.page}") int transactionsPerPage,
                              ExchangeRateService exchangeRateService) {
        this.kafkaConsumer = kafkaConsumer;
        this.transactionsPerPage = transactionsPerPage;
        this.exchangeRateService = exchangeRateService;
    }

    public CustomerController() {
    }

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

        int totalPages = (int) Math.ceil(transactions.size() / (double) transactionsPerPage);
        int fromIndex = (pageNumber - 1) * transactionsPerPage;
        int toIndex = Math.min(fromIndex + transactionsPerPage, transactions.size());
        List<Transaction> paginatedTransactions = transactions.subList(fromIndex, toIndex);
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
