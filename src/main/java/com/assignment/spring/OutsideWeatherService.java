package com.assignment.spring;

import com.assignment.spring.api.WeatherResponse;
import com.assignment.spring.client.OpenWeatherClient;
import com.assignment.spring.data.WeatherEntity;
import com.assignment.spring.data.WeatherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OutsideWeatherService {
    private static final Logger LOG = LoggerFactory.getLogger(OutsideWeatherService.class);
    private final WeatherRepository weatherRepository;
    private final OpenWeatherClient openWeatherClient;

    public OutsideWeatherService(WeatherRepository weatherRepository,
            OpenWeatherClient openWeatherClient) {
        this.weatherRepository = weatherRepository;
        this.openWeatherClient = openWeatherClient;
    }

    public WeatherEntity readFromOutsideAndSave(String city) {
        LOG.debug("Calling outside service for city {}", city);
        WeatherEntity weather = map(openWeatherClient.weatherForCity(city));
        weatherRepository.save(weather);
        return weather;
    }

    private WeatherEntity map(WeatherResponse response) {
        WeatherEntity entity = new WeatherEntity();
        entity.setCity(response.getName());
        entity.setCountry(response.getSys().getCountry());
        entity.setTemperature(BigDecimal.valueOf(response.getMain().getTemp()));
        entity.setUpdateTime(LocalDateTime.now());
        return entity;
    }
}
