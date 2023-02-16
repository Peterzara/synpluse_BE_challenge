package com.example.techChallenge.exchangeRate;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ExchangeRateService {

    private String baseCurrency = "HKD";

    @Value("${exchange.api.password}")
    private String API_KEY;

    private final RestTemplateBuilder restTemplateBuilder;
    private RestTemplate restTemplate;

    @PostConstruct
    public void init() {
        restTemplate = restTemplateBuilder.build();
    }

    public double getExchangeRate(String currency) {
        String url = "https://api.apilayer.com/exchangerates_data/latest?base=" + baseCurrency + "&symbols=" + currency;
        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", API_KEY);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        String exchangeRate = response.getBody();
        double rate = parseExchangeRateFromResponse(exchangeRate, currency);
        return rate;
    }

    public double getFakeExchangeRate(String baseCurrency) {
        return 1;
    }

    private double parseExchangeRateFromResponse(String exchangeRate, String currency) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root;
        try {
            root = mapper.readTree(exchangeRate);
            JsonNode rate = root.path("rates").path(currency);
            return rate.asDouble();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}
