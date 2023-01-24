package com.example.techChallenge.transcationKafkaConfig;

import com.example.techChallenge.transaction.Transaction;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service // CustomerService
public class KafkaConsumer {

    private final KafkaTemplate<String, Transaction> kafkaTemplate;
    private final Map<String, List<Transaction>> transactionsByCustomer;

    public KafkaConsumer(KafkaTemplate<String, Transaction> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.transactionsByCustomer = new HashMap<>();
    }

    @KafkaListener(topics = "transactions", groupId = "groupId", containerFactory = "factory")
    public void consumeTransaction(ConsumerRecord<String, Transaction> record) {
        Transaction transaction = record.value();
        String customerId = transaction.getCid();
        List<Transaction> transactions = transactionsByCustomer.get(customerId);
        if (transactions == null) {
            transactions = new ArrayList<>();
        }
        transactions.add(transaction);
        transactionsByCustomer.put(customerId, transactions);
    }

    public List<Transaction> getTransactionsByCustomerIDAndMonth(String customerId, int month) {
        List<Transaction> transactions = transactionsByCustomer.get(customerId);
        if (transactions == null) {
            return null;
        }

        List<Transaction> filteredTransactions = new ArrayList<>();
        for (Transaction transaction : transactions) {
            LocalDate transactionDate = transaction.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (transactionDate.get(ChronoField.MONTH_OF_YEAR) == month) {
                filteredTransactions.add(transaction);
            }
        }
        return filteredTransactions;
    }
}



