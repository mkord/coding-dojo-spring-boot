package com.assignment.spring;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.assignment.spring.api.Main;
import com.assignment.spring.api.Sys;
import com.assignment.spring.api.WeatherResponse;
import com.assignment.spring.client.OpenWeatherClient;
import com.assignment.spring.data.WeatherEntity;
import com.assignment.spring.data.WeatherRepository;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class WeatherServiceTest {
    private static final String CITY_KRAKOW = "Krakow";
    private final WeatherEntity someWeather = createWeather();
    private final WeatherResponse someWeatherResponse = createWeatherResponse();
    private final WeatherRepository weatherRepository = mock(WeatherRepository.class);
    private final OpenWeatherClient openWeatherClient = mock(OpenWeatherClient.class);
    private final WeatherService weatherService =
            new WeatherService(weatherRepository, openWeatherClient);

    @Test
    void testReadWeatherFromDbWithoutCallingExternalService() {
        when(weatherRepository.findByCity(CITY_KRAKOW)).thenReturn(List.of(someWeather));
        weatherService.readWeatherForCity(CITY_KRAKOW);
        verify(weatherRepository).findByCity(CITY_KRAKOW);
        verifyNoInteractions(openWeatherClient);
    }

    @Test
    void testReadWeatherFromExternalServiceIfNoWeatherInDb() {
        when(weatherRepository.findByCity(CITY_KRAKOW)).thenReturn(List.of());
        when(openWeatherClient.weatherForCity(CITY_KRAKOW)).thenReturn(someWeatherResponse);
        weatherService.readWeatherForCity(CITY_KRAKOW);
        verify(weatherRepository).findByCity(CITY_KRAKOW);
        verify(openWeatherClient).weatherForCity(CITY_KRAKOW);
    }

    private WeatherEntity createWeather() {
        WeatherEntity entity = new WeatherEntity();
        entity.setCity(CITY_KRAKOW);
        entity.setCountry("PL");
        entity.setTemperature(BigDecimal.TEN);
        return entity;
    }

    private WeatherResponse createWeatherResponse() {
        WeatherResponse response = new WeatherResponse();
        response.setName(CITY_KRAKOW);
        Sys sys = new Sys();
        sys.setCountry("PL");
        response.setSys(sys);
        Main main = new Main();
        main.setTemp(10d);
        response.setMain(main);
        return response;
    }
}