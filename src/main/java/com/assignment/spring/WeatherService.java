package com.assignment.spring;

import com.assignment.spring.api.WeatherResponse;
import com.assignment.spring.client.OpenWeatherClient;
import com.assignment.spring.data.WeatherEntity;
import com.assignment.spring.data.WeatherRepository;
import java.math.BigDecimal;
import java.util.List;

public class WeatherService {
    private final WeatherRepository weatherRepository;
    private final OpenWeatherClient openWeatherClient;

    public WeatherService(WeatherRepository weatherRepository, OpenWeatherClient openWeatherClient) {
        this.weatherRepository = weatherRepository;
        this.openWeatherClient = openWeatherClient;
    }

    public WeatherEntity readWeatherForCity(String city) {
        List<WeatherEntity> entities = weatherRepository.findByCity(city);
        if (entities == null || entities.isEmpty()) {
            return readFromOutsideAndSave(city);
        }
        return entities.get(0);
    }

    private WeatherEntity readFromOutsideAndSave(String city) {
        WeatherEntity weather = map(openWeatherClient.weatherForCity(city));
        weatherRepository.save(weather);
        return weather;
    }

    private WeatherEntity map(WeatherResponse response) {
        WeatherEntity entity = new WeatherEntity();
        entity.setCity(response.getName());
        entity.setCountry(response.getSys().getCountry());
        entity.setTemperature(BigDecimal.valueOf(response.getMain().getTemp()));
        return entity;
    }
}
