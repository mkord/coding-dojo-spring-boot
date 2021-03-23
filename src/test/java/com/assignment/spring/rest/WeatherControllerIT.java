package com.assignment.spring.rest;

import com.assignment.spring.Application;
import com.assignment.spring.TestConfig;
import com.assignment.spring.api.WeatherResponse;
import com.assignment.spring.client.OpenWeatherClient;
import com.assignment.spring.data.WeatherEntity;
import com.assignment.spring.data.WeatherRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {Application.class, TestConfig.class})
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WeatherControllerIT {
    private static final String OPEN_WEATHER_SAMPLE_RESPONSE = "{\"coord\":{\"lon\":19.9167,\"lat\":50.0833},\"weather\":[{\"id\":803,\"main\":\"Clouds\",\"description\":\"broken clouds\",\"icon\":\"04n\"}],\"base\":\"stations\",\"main\":{\"temp\":273.66,\"pressure\":1022,\"humidity\":80,\"temp_min\":272.59,\"temp_max\":274.26,\"feels_like\":269.53},\"visibility\":10000,\"wind\":{\"speed\":2.57,\"deg\":310},\"clouds\":{\"all\":75},\"dt\":1614965186,\"sys\":{\"type\":1,\"id\":1701,\"country\":\"PL\",\"sunrise\":1614921334,\"sunset\":1614961703},\"id\":3094802,\"name\":\"Krakow\",\"cod\":200,\"timezone\":3600}";
    public static final String ERROR_MESSAGE = "Something unpredictable happens";
    public static final String WEATHER_ENDPOINT = "/weather";
    public static final String CITY_KRAKOW = "Krakow";
    private WeatherResponse openWeatherSampleResponse;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WeatherRepository weatherRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OpenWeatherClient openWeatherClient;

    @BeforeAll
    void setUp() {
        openWeatherSampleResponse = parseWeatherResponse();
    }

    @Test
    @Transactional
    void shouldReadWeatherDataFromOpenWeatherAndSaveItToDb() throws Exception {
        when(openWeatherClient.weatherForCity(anyString())).thenReturn(openWeatherSampleResponse);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(WEATHER_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("city", CITY_KRAKOW))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.city", equalTo(CITY_KRAKOW)))
                .andExpect(jsonPath("$.country", equalTo("PL")))
                .andExpect(jsonPath("$.temperature", equalTo(273.66d)))
                .andReturn();
        WeatherEntity expectedWeather = extractFromMvcResult(mvcResult);
        Optional<WeatherEntity> probablyWeather = weatherRepository.findById(expectedWeather.getId());
        assertThat(probablyWeather).isPresent();
        WeatherEntity weatherFromDb = probablyWeather.get();
        assertThat(weatherFromDb.getCity()).isEqualTo(expectedWeather.getCity());
        assertThat(weatherFromDb.getCountry()).isEqualTo(expectedWeather.getCountry());
        assertThat(weatherFromDb.getTemperature()).isEqualTo(expectedWeather.getTemperature());
        assertThat(weatherFromDb.getUpdateTime()).isEqualTo(expectedWeather.getUpdateTime());
    }

    @Test
    void shouldReturn500ErrorAndDoNotSaveAnythingToDb() throws Exception {
        when(openWeatherClient.weatherForCity(anyString())).thenThrow(
                new RuntimeException(ERROR_MESSAGE));
        mockMvc.perform(MockMvcRequestBuilders.get(WEATHER_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("city", CITY_KRAKOW))
                .andExpect(MockMvcResultMatchers.status().is5xxServerError())
                .andExpect(jsonPath("$.message", equalTo(ERROR_MESSAGE)));
        assertThat(weatherRepository.findAll()).isEmpty();
    }

    private WeatherResponse parseWeatherResponse() {
        try {
            return objectMapper.readValue(OPEN_WEATHER_SAMPLE_RESPONSE, WeatherResponse.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot parse sample openWeather JSON response");
        }
    }

    private WeatherEntity extractFromMvcResult(MvcResult mvcResult)
            throws UnsupportedEncodingException, JsonProcessingException {
        return objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), WeatherEntity.class);
    }
}
