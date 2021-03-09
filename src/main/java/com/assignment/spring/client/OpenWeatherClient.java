package com.assignment.spring.client;

import com.assignment.spring.api.WeatherResponse;
import com.assignment.spring.config.OpenWeatherApiProperties;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class OpenWeatherClient {
    private final OpenWeatherApiProperties apiProperties;
    private final RestTemplate restTemplate;

    @Autowired
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
