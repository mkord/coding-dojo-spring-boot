package com.assignment.spring.client;

import com.assignment.spring.api.WeatherResponse;
import com.assignment.spring.config.OpenWeatherApiProperties;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public class OpenWeatherClient {
    private final OpenWeatherApiProperties apiProperties;
    private final RestTemplate restTemplate;

    public OpenWeatherClient(OpenWeatherApiProperties apiProperties, RestTemplate restTemplate) {
        this.apiProperties = apiProperties;
        this.restTemplate = restTemplate;
    }

    public WeatherResponse weatherForCity(String city) {
        return restTemplate.getForEntity(buildUri(city), WeatherResponse.class).getBody();
    }

    private URI buildUri(String city) {
        return UriComponentsBuilder.fromHttpUrl(apiProperties.getUrl())
                .queryParam("q", city)
                .queryParam("APPID", apiProperties.getAppId())
                .build()
                .toUri();
    }

}
