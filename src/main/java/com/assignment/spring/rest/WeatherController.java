package com.assignment.spring.rest;

import com.assignment.spring.api.WeatherResponse;
import com.assignment.spring.client.OpenWeatherClient;
import com.assignment.spring.data.WeatherEntity;
import com.assignment.spring.data.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class WeatherController {
    private final OpenWeatherClient openWeatherClient;
    private final WeatherRepository weatherRepository;

    @Autowired
    public WeatherController(OpenWeatherClient openWeatherClient, WeatherRepository weatherRepository) {
        this.openWeatherClient = openWeatherClient;
        this.weatherRepository = weatherRepository;
    }

    @RequestMapping("/weather")
    public WeatherEntity weather(@RequestParam String city) {
        WeatherEntity weather = map(openWeatherClient.weatherForCity(city));
        weatherRepository.save(weather);
        return weather;
    }

    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<Exception> handleAllExceptions(RuntimeException e) {
        return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private WeatherEntity map(WeatherResponse response) {
        WeatherEntity entity = new WeatherEntity();
        entity.setCity(response.getName());
        entity.setCountry(response.getSys().getCountry());
        entity.setTemperature(BigDecimal.valueOf(response.getMain().getTemp()));
        return entity;
    }
}
