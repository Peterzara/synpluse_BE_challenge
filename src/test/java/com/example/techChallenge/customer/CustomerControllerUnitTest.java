package com.example.techChallenge.customer;

import com.example.techChallenge.transaction.Transaction;
import com.example.techChallenge.exchangeRate.ExchangeRateService;
import com.example.techChallenge.transaction.TransactionResponse;
import com.example.techChallenge.transcationKafkaConfig.KafkaConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerControllerUnitTest {
    @InjectMocks
    private CustomerController customerController;

    @Mock
    private KafkaConsumer kafkaConsumer;

    @Mock
    private ExchangeRateService exchangeRateService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2023);
        calendar.set(Calendar.MONTH, 10);
        calendar.set(Calendar.DATE, 1);
        Date date = calendar.getTime();
        transactions.add(new Transaction("0be4f56f-0368-4c8e-94e8-bcaa7c0327fa", "CUST96652", 100.0, date, "CH40-83491705496983474","Transaction CUST96652 #239","USD", 11));
        transactions.add(new Transaction("0be4f56f-0368-4c8e-94e8-bcaa7c0327fb", "CUST96652", -50, date, "CH40-83491705496983474","Transaction CUST96652 #239","USD", 11));
        transactions.add(new Transaction("0be4f56f-0368-4c8e-94e8-bcaa7c0327fc", "CUST96652", 75.0, date, "CH40-83491705496983474","Transaction CUST96652 #239","USD", 11));
        when(kafkaConsumer.getTransactionsByCustomerIDAndMonth("CUST96652", 11)).thenReturn(transactions);
        when(exchangeRateService.getExchangeRate("USD")).thenReturn(1.0);

        ResponseEntity<TransactionResponse> response = customerController.getTransactions("CUST96652", 11, 1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(3, response.getBody().getTransactions().size());
        assertEquals(175.0, response.getBody().getTotalCredit());
        assertEquals(50.0, response.getBody().getTotalDebit());
        assertEquals(1, response.getBody().getCurrentPage());
        assertEquals(1, response.getBody().getTotalPages());
    }
}
