package com.assignment.spring;

import com.assignment.spring.config.WeatherUpdateProperties;
import com.assignment.spring.data.WeatherEntity;
import com.assignment.spring.data.WeatherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class WeatherUpdateService {
    private static final Logger LOG = LoggerFactory.getLogger(WeatherUpdateService.class);
    private final WeatherUpdateProperties weatherUpdateProperties;
    private final WeatherRepository weatherRepository;
    private final OutsideWeatherService outsideWeatherService;

    public WeatherUpdateService(WeatherUpdateProperties weatherUpdateProperties,
            WeatherRepository weatherRepository,
            OutsideWeatherService outsideWeatherService) {
        this.weatherUpdateProperties = weatherUpdateProperties;
        this.weatherRepository = weatherRepository;
        this.outsideWeatherService = outsideWeatherService;
    }

    @Scheduled(fixedDelayString = "${app.weather.update.fixedDelayInMilliseconds}", initialDelay = 10000)
    public void updateWeather() {
        List<WeatherEntity> weatherToUpdate = readWeatherToUpdate();
        if (weatherToUpdate.size() > weatherUpdateProperties.getMaxCallsPerMinutes()) {
            LOG.warn("There are more cities to update then max allowed, only {} will be updated",
                    weatherUpdateProperties.getMaxCallsPerMinutes());
        }
        LOG.debug("Going to update weather for {} cities", weatherToUpdate.size());
        weatherToUpdate.stream()
                .limit(weatherUpdateProperties.getMaxCallsPerMinutes())
                .forEach(weather -> outsideWeatherService.readFromOutsideAndSave(weather.getCity()));
    }

    private List<WeatherEntity> readWeatherToUpdate() {
        LocalDateTime olderThen = LocalDateTime.now()
                .minus(weatherUpdateProperties.getUpdateOlderThenInMinutes(), ChronoUnit.MINUTES);
        LOG.debug("Reading cities with updateTime after {}", olderThen);
        return weatherRepository.findByUpdateTimeIsBefore(olderThen);
    }
}
