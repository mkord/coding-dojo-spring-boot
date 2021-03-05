package com.assignment.spring.config;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OpenWeatherApiPropertiesTest {
    private static final OpenWeatherApiProperties OPEN_WEATHER_API_PROPERTIES = new OpenWeatherApiProperties();
    private static final String URL = "SomeUrl";
    private static final String APP_ID = "SomeAppId";

    @BeforeAll
    static void setUp() {
        OPEN_WEATHER_API_PROPERTIES.setAppId(APP_ID);
        OPEN_WEATHER_API_PROPERTIES.setUrl(URL);
    }

    @Test
    void shouldReturnUrl() {
        assertThat(OPEN_WEATHER_API_PROPERTIES.getUrl()).isEqualTo(URL);
    }

    @Test
    void shouldReturnAppId() {
        assertThat(OPEN_WEATHER_API_PROPERTIES.getAppId()).isEqualTo(APP_ID);
    }
}