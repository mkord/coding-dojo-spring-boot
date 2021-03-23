package com.assignment.spring.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.weather.update")
public class WeatherUpdateProperties {
    private int updateOlderThenInMinutes;
    private int maxCallsPerMinutes;
    private int fixedDelayInMilliseconds;

    public int getFixedDelayInMilliseconds() {
        return fixedDelayInMilliseconds;
    }

    public void setFixedDelayInMilliseconds(int fixedDelayInMilliseconds) {
        this.fixedDelayInMilliseconds = fixedDelayInMilliseconds;
    }

    public int getUpdateOlderThenInMinutes() {
        return updateOlderThenInMinutes;
    }

    public void setUpdateOlderThenInMinutes(int updateOlderThenInMinutes) {
        this.updateOlderThenInMinutes = updateOlderThenInMinutes;
    }

    public int getMaxCallsPerMinutes() {
        return maxCallsPerMinutes;
    }

    public void setMaxCallsPerMinutes(int maxCallsPerMinutes) {
        this.maxCallsPerMinutes = maxCallsPerMinutes;
    }
}
