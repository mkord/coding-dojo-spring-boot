package com.assignment.spring;

import com.assignment.spring.api.WeatherResponse;
import com.assignment.spring.client.OpenWeatherClient;
import com.assignment.spring.data.WeatherEntity;
import com.assignment.spring.data.WeatherRepository;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OutsideWeatherService {
    private static final Logger LOG = LoggerFactory.getLogger(OutsideWeatherService.class);
    private final WeatherRepository weatherRepository;
    private final OpenWeatherClient openWeatherClient;

    public OutsideWeatherService(WeatherRepository weatherRepository,
            OpenWeatherClient openWeatherClient) {
        this.weatherRepository = weatherRepository;
        this.openWeatherClient = openWeatherClient;
    }

    @HystrixCommand(fallbackMethod = "readFromOutsideAndSaveBackup")
    public WeatherEntity readFromOutsideAndSave(String city) {
        LOG.debug("Calling outside service for city {}", city);
        WeatherEntity weather = map(openWeatherClient.weatherForCity(city));
        weatherRepository.save(weather);
        return weather;
    }

    private WeatherEntity readFromOutsideAndSaveBackup(String city) {
        List<WeatherEntity> weatherForCity = weatherRepository.findByCity(city);
        WeatherEntity weather;
        if (weatherForCity.isEmpty()) {
            weather = new WeatherEntity();
            weather.setCity(city);
        } else {
            weather = weatherForCity.get(0);
            weather.setUpdateTime(LocalDateTime.now());
            weatherRepository.save(weather);
        }
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
