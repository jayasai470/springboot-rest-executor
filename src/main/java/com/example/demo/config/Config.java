package com.example.demo.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Config {

    @Bean(name = "coinBaseCurrencies")
    public RestTemplate coinBaseCurrencies(RestTemplateBuilder restTemplateBuilder){
        return restTemplateBuilder.rootUri("https://api.coinbase.com")
                .build();
    }

    @Bean(name = "coinBaseExchangeRate")
    public RestTemplate coinBaseExchangeRate(RestTemplateBuilder restTemplateBuilder){
        return restTemplateBuilder.rootUri("https://api.coinbase.com")
                .build();
    }

    @Bean(name = "coinBasePrices")
    public RestTemplate coinBasePrices(RestTemplateBuilder restTemplateBuilder){
        return restTemplateBuilder.rootUri("https://api.coinbase.com")
                .build();
    }
}
