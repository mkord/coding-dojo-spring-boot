package com.assignment.spring.client;

import com.assignment.spring.api.WeatherResponse;
import com.assignment.spring.config.OpenWeatherApiProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OpenWeatherClientTest {
    private final static String APP_ID = "someAppId";
    private final OpenWeatherApiProperties openWeatherApiProperties = mock(OpenWeatherApiProperties.class);
    private final RestTemplate restTemplate = mock(RestTemplate.class);
    private OpenWeatherClient openWeatherClient = new OpenWeatherClient(openWeatherApiProperties, restTemplate);

    @BeforeEach
    void setUpMocks() {
        when(openWeatherApiProperties.getUrl()).thenReturn("http://something.test");
        when(openWeatherApiProperties.getAppId()).thenReturn(APP_ID);
        when(restTemplate.getForEntity(any(URI.class), eq(WeatherResponse.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
    }

    @Test
    void shouldPassCorrectCityAndAppIdToRestTemplate() {
        String city = "SampleCity";
        openWeatherClient.weatherForCity(city);
        ArgumentCaptor<URI> uriArgumentCaptor = ArgumentCaptor.forClass(URI.class);
        verify(restTemplate).getForEntity(uriArgumentCaptor.capture(), eq(WeatherResponse.class));
        assertThat(uriArgumentCaptor.getValue().getQuery())
                .contains("q=" + city)
                .contains("APPID=" + APP_ID);
    }
}