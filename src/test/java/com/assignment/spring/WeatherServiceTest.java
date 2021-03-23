package com.assignment.spring;

import com.assignment.spring.data.WeatherEntity;
import com.assignment.spring.data.WeatherRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class WeatherServiceTest {
    private static final String CITY_KRAKOW = "Krakow";
    private final WeatherEntity someWeather = createWeather();
    private final WeatherRepository weatherRepository = mock(WeatherRepository.class);
    private final OutsideWeatherService outsideWeatherService = mock(OutsideWeatherService.class);
    private final WeatherService weatherService =
            new WeatherService(weatherRepository, outsideWeatherService);

    @Test
    void testReadWeatherFromDbWithoutCallingExternalService() {
        when(weatherRepository.findByCity(CITY_KRAKOW)).thenReturn(List.of(someWeather));
        weatherService.readWeatherForCity(CITY_KRAKOW);
        verify(weatherRepository).findByCity(CITY_KRAKOW);
        verifyNoInteractions(outsideWeatherService);
    }

    @Test
    void testReadWeatherFromExternalServiceIfNoWeatherInDb() {
        when(weatherRepository.findByCity(CITY_KRAKOW)).thenReturn(List.of());
        when(outsideWeatherService.readFromOutsideAndSave(CITY_KRAKOW)).thenReturn(someWeather);
        weatherService.readWeatherForCity(CITY_KRAKOW);
        verify(weatherRepository).findByCity(CITY_KRAKOW);
        verify(outsideWeatherService).readFromOutsideAndSave(CITY_KRAKOW);
    }

    private WeatherEntity createWeather() {
        WeatherEntity entity = new WeatherEntity();
        entity.setCity(CITY_KRAKOW);
        entity.setCountry("PL");
        entity.setTemperature(BigDecimal.TEN);
        return entity;
    }
}
