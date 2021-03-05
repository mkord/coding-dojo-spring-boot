package com.assignment.spring.config;

import com.assignment.spring.client.OpenWeatherClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties
public class AppConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public OpenWeatherApiProperties openWeatherApiProperties() {
        return new OpenWeatherApiProperties();
    }

    @Bean
    public OpenWeatherClient openWeatherClient() {
        return new OpenWeatherClient(openWeatherApiProperties(), restTemplate());
    }
}
