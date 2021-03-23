package com.assignment.spring;

import com.assignment.spring.api.Main;
import com.assignment.spring.api.Sys;
import com.assignment.spring.api.WeatherResponse;
import com.assignment.spring.client.OpenWeatherClient;
import com.assignment.spring.data.WeatherEntity;
import com.assignment.spring.data.WeatherRepository;
import org.assertj.core.data.TemporalUnitLessThanOffset;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OutsideWeatherServiceTest {
    private static final String CITY_KRAKOW = "Krakow";
    private static final String COUNTRY_PL = "PL";
    private static final double SOME_TEMP = 10d;
    private final WeatherResponse someWeatherResponse = createWeatherResponse();
    private final WeatherRepository weatherRepository = mock(WeatherRepository.class);
    private final OpenWeatherClient openWeatherClient = mock(OpenWeatherClient.class);
    private final OutsideWeatherService outsideWeatherService =
            new OutsideWeatherService(weatherRepository, openWeatherClient);
    @Test
    void testReadAndSaveWeatherForCity() {
        when(openWeatherClient.weatherForCity(CITY_KRAKOW)).thenReturn(someWeatherResponse);
        outsideWeatherService.readFromOutsideAndSave(CITY_KRAKOW);
        verify(openWeatherClient).weatherForCity(CITY_KRAKOW);
        ArgumentCaptor<WeatherEntity> entityArgumentCaptor = ArgumentCaptor.forClass(WeatherEntity.class);
        verify(weatherRepository).save(entityArgumentCaptor.capture());
        assertThat(entityArgumentCaptor.getValue().getCity()).isEqualTo(CITY_KRAKOW);
        assertThat(entityArgumentCaptor.getValue().getCountry()).isEqualTo(COUNTRY_PL);
        assertThat(entityArgumentCaptor.getValue().getTemperature()).isEqualTo(BigDecimal.valueOf(SOME_TEMP));
        assertThat(entityArgumentCaptor.getValue().getUpdateTime())
                .isCloseTo(LocalDateTime.now(), new TemporalUnitLessThanOffset(100, ChronoUnit.MILLIS));
    }

    private WeatherResponse createWeatherResponse() {
        WeatherResponse response = new WeatherResponse();
        response.setName(CITY_KRAKOW);
        Sys sys = new Sys();
        sys.setCountry(COUNTRY_PL);
        response.setSys(sys);
        Main main = new Main();
        main.setTemp(SOME_TEMP);
        response.setMain(main);
        return response;
    }
}
