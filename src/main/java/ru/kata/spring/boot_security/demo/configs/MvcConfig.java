package ru.kata.spring.boot_security.demo.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class MvcConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}