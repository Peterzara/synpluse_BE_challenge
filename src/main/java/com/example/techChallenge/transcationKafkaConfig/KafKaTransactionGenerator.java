package com.example.techChallenge.transcationKafkaConfig;

import com.example.techChallenge.transaction.Transaction;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
public class KafKaTransactionGenerator {
//    private final Logger logger = LoggerFactory.getLogger(KafKaTransactionGenerator.class);
//    @Value("${testing.generate.transactions}")
    private boolean isTesting = true; // Here is a bug that couldn't load the property from application-test.properties properly
    private String[] currencies = {"USD", "EUR", "GBP", "CHF", "JPY"};
    public List<Transaction> generatedTransactionsForTesting = new ArrayList<>();

    public KafKaTransactionGenerator() {
    }

    /***
     * generate random trans to topic
     * @param numTransactions
     */
    public void generateRandomTransactionsForUser(KafkaTemplate<String, Transaction> kafkaTemplate, int numTransactions, String customerId, Random rand) {
        for (int i = 0; i < numTransactions; i++) {


            String transactionId = UUID.randomUUID().toString();
            String currency = currencies[rand.nextInt(currencies.length)];
            double amount = rand.nextDouble() * 10000;
            String iban = generateIBAN(rand);
            Date valueDate = Date.from(LocalDate.now().minusDays(rand.nextInt(365*10)).atStartOfDay(ZoneId.systemDefault()).toInstant());
            String description = "Transaction " + customerId + " #" + i;


            Transaction transaction = new Transaction(transactionId, customerId, amount, valueDate, iban, description, currency, getMonth(valueDate));

            if(isTesting) {
                generatedTransactionsForTesting.add(transaction);
            }

            kafkaTemplate.send("transactions", transactionId, transaction);

        }
    }

    private int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1;
        return month;
    }

    private static final String IBAN_FORMAT = "CH%02d%012d";
    private static final int IBAN_LENGTH = 21;
    private static final int IBAN_FORMAT_LENGTH = IBAN_LENGTH - 2;

    /***
     * helper func: generate a random IBAN
     * @param random
     * @return String
     */
    private String generateIBAN(Random random) {
        int bankCode = random.nextInt(99);
        long accountNumber = random.nextLong() % (long) Math.pow(10, IBAN_FORMAT_LENGTH - 2);
        return String.format(IBAN_FORMAT, bankCode, accountNumber);
    }

}
