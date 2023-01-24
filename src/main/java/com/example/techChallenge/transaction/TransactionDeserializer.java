package com.example.techChallenge.transaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;

public class TransactionDeserializer implements Deserializer<Transaction> {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Transaction deserialize(String topic, byte[] data) {
        Transaction transaction = null;
        try {
            transaction = objectMapper.readValue(data, Transaction.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return transaction;
    }
}
