package com.example.techChallenge;

import com.example.techChallenge.transaction.Transaction;
import com.example.techChallenge.transcationKafkaConfig.KafKaTransactionGenerator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Random;

@SpringBootApplication
public class TechChallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(TechChallengeApplication.class, args);
	}

	/** EXPLAIN:
	 * The function returns an instance of CommandLineRunner (interface), which only has one abstract method
	 * , we thus can pass a lambda function that implements the run() method of the CommandLineRunner
	 * interface.
	 * @param kafkaTemplate
	 * @param transactionGenerator
	 * @return instance of CommandLineRunner
	 */
	@Bean
	CommandLineRunner commandLineRunner(KafkaTemplate<String, Transaction> kafkaTemplate, KafKaTransactionGenerator transactionGenerator) {
		return args -> {

			int userNum = 3; // 100000
			int totalTransPerUser = 10; // 240000

			for(int i=0; i < userNum; i++) {
				Random rand = new Random();
				String customerId = "CUST" + rand.nextInt(100000);
				transactionGenerator.generateRandomTransactionsForUser(kafkaTemplate, totalTransPerUser, customerId, rand);
			}
		};
	}
}
