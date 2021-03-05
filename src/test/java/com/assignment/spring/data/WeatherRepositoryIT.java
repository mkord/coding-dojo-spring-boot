package com.assignment.spring.data;

import com.assignment.spring.Application;
import com.assignment.spring.TestConfig;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class, TestConfig.class})
@Testcontainers
class WeatherRepositoryIT {

    @Autowired
    private WeatherRepository weatherRepository;

    @Test
    @Transactional
    void shouldSaveWeatherEntityToDb() {
        //given
        WeatherEntity entity = createSampleWeather();
        //when
        weatherRepository.save(entity);
        //then
        Optional<WeatherEntity> probablyWeather = weatherRepository.findById(entity.getId());
        assertThat(probablyWeather).isPresent();
        WeatherEntity weatherFromDb = probablyWeather.get();
        assertThat(weatherFromDb).isEqualToComparingFieldByField(entity);
    }

    private WeatherEntity createSampleWeather() {
        WeatherEntity entity = new WeatherEntity();
        entity.setCity("SampleTestCity");
        entity.setCountry("SampleTestCountry");
        entity.setTemperature(BigDecimal.valueOf(156.66d));
        return entity;
    }
}