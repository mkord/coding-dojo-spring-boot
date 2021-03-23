package com.assignment.spring;

import com.assignment.spring.data.WeatherEntity;
import com.assignment.spring.data.WeatherRepository;

import java.util.List;

public class WeatherService {
    private final WeatherRepository weatherRepository;
    private final OutsideWeatherService outsideWeatherService;

    public WeatherService(WeatherRepository weatherRepository, OutsideWeatherService outsideWeatherService) {
        this.weatherRepository = weatherRepository;
        this.outsideWeatherService = outsideWeatherService;
    }

    public WeatherEntity readWeatherForCity(String city) {
        List<WeatherEntity> entities = weatherRepository.findByCity(city);
        if (entities == null || entities.isEmpty()) {
            return outsideWeatherService.readFromOutsideAndSave(city);
        }
        return entities.get(0);
    }
}
