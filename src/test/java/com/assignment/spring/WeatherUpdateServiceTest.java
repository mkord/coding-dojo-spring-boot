package com.assignment.spring;

import com.assignment.spring.config.WeatherUpdateProperties;
import com.assignment.spring.data.WeatherEntity;
import com.assignment.spring.data.WeatherRepository;
import org.assertj.core.data.TemporalUnitLessThanOffset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class WeatherUpdateServiceTest {
    private static final int OLDER_THEN_FIVE_MINUTES = 5;
    private final WeatherUpdateProperties updateProperties = mock(WeatherUpdateProperties.class);
    private final WeatherRepository weatherRepository = mock(WeatherRepository.class);
    private final OutsideWeatherService outsideWeatherService = mock(OutsideWeatherService.class);
    private WeatherUpdateService weatherUpdateService =
            new WeatherUpdateService(updateProperties, weatherRepository, outsideWeatherService);

    @BeforeEach
    void setUp() {
        when(updateProperties.getMaxCallsPerMinutes()).thenReturn(2);
        when(updateProperties.getUpdateOlderThenInMinutes()).thenReturn(OLDER_THEN_FIVE_MINUTES);
    }

    @Test
    void testUpdateWeatherWithCorrectDateTime() {
        weatherUpdateService.updateWeather();
        ArgumentCaptor<LocalDateTime> argumentCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
        verify(weatherRepository).findByUpdateTimeIsBefore(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue())
                .isCloseTo(expectedDateTime(), new TemporalUnitLessThanOffset(1, ChronoUnit.SECONDS));
    }

    @Test
    void testUpdateWeatherCallForUpdateForEach() {
        when(weatherRepository.findByUpdateTimeIsBefore(any())).thenReturn(someWeatherToUpdate());
        weatherUpdateService.updateWeather();
        verify(outsideWeatherService).readFromOutsideAndSave("KRAKOW");
        verify(outsideWeatherService).readFromOutsideAndSave("BOGOTA");
    }

    private List<WeatherEntity> someWeatherToUpdate() {
        WeatherEntity weatherKrakow = new WeatherEntity();
        weatherKrakow.setCity("KRAKOW");
        WeatherEntity weatherBogota = new WeatherEntity();
        weatherBogota.setCity("BOGOTA");
        return List.of(weatherKrakow, weatherBogota);
    }

    private LocalDateTime expectedDateTime() {
        return LocalDateTime.now().minus(OLDER_THEN_FIVE_MINUTES, ChronoUnit.MINUTES);
    }
}
