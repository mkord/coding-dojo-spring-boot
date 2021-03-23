package com.assignment.spring.config;

import com.assignment.spring.OutsideWeatherService;
import com.assignment.spring.WeatherService;
import com.assignment.spring.WeatherUpdateService;
import com.assignment.spring.client.OpenWeatherClient;
import com.assignment.spring.data.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties
@EnableScheduling
public class AppConfig {
    private final WeatherRepository weatherRepository;

    @Autowired
    public AppConfig(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public OpenWeatherApiProperties openWeatherApiProperties() {
        return new OpenWeatherApiProperties();
    }

    @Bean
    public WeatherUpdateProperties weatherUpdateProperties() {
        return new WeatherUpdateProperties();
    }

    @Bean
    public OpenWeatherClient openWeatherClient() {
        return new OpenWeatherClient(openWeatherApiProperties(), restTemplate());
    }

    @Bean
    public OutsideWeatherService outsideWeatherService() {
        return new OutsideWeatherService(weatherRepository, openWeatherClient());
    }

    @Bean
    public WeatherService weatherService() {
        return new WeatherService(weatherRepository, outsideWeatherService());
    }

    @Bean
    public WeatherUpdateService weatherUpdateService() {
        return new WeatherUpdateService(weatherUpdateProperties(), weatherRepository, outsideWeatherService());
    }
}
