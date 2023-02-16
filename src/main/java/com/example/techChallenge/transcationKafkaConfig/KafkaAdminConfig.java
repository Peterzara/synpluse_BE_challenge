package com.example.techChallenge.transcationKafkaConfig;

import com.example.techChallenge.transaction.Transaction;
import com.example.techChallenge.transaction.TransactionJsonSerializer;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@Configuration
public class KafkaAdminConfig {

//    @Value("${spring.kafka.bootstrap-servers}")
    @Value("#{environment['spring.kafka.bootstrap-servers'] ?: 'localhost:9092'}")
    private String bootstrapServers;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic transactionsTopic() throws ExecutionException, InterruptedException {
        final String topicName = "transactions";
        final int numPartitions = 1;
        final short replicationFactor = 1;
        AdminClient admin = AdminClient.create(kafkaAdmin().getConfigurationProperties());
        ListTopicsResult topics = admin.listTopics();
        Set<String> topicNames = topics.names().get();
        if (!topicNames.contains(topicName)) {
            NewTopic newTopic = new NewTopic(topicName, numPartitions, replicationFactor);
            admin.createTopics(Collections.singleton(newTopic));
        }
        admin.close();
        return new NewTopic(topicName, numPartitions, replicationFactor);
    }

    @Bean
    public KafkaTemplate<String, Transaction> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ProducerFactory<String, Transaction> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, TransactionJsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps, new StringSerializer(), new TransactionJsonSerializer());
    }
}

