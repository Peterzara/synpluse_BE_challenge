package com.example.techChallenge.transcationKafkaConfig;

import com.example.techChallenge.transaction.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class KafKaTransactionGeneratorUnitTest {

    @Mock
    private KafkaTemplate<String, Transaction> kafkaTemplate;

    @InjectMocks
    private KafKaTransactionGenerator kafKaTransactionGenerator;
    private Random rand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
//        kafKaTransactionGenerator = new KafKaTransactionGenerator();
        rand = new Random();
    }

    @Test
    void generateRandomTransactionsForUser() {
        int numTransactions = 10;
        String customerId = "CUST96652";
        kafKaTransactionGenerator.generateRandomTransactionsForUser(kafkaTemplate, numTransactions, customerId, rand);
        assertThat(kafKaTransactionGenerator.generatedTransactionsForTesting, hasSize(numTransactions));
        assertThat(kafKaTransactionGenerator.generatedTransactionsForTesting.get(0).getAmount(), greaterThan(0.0));
    }
}
