package com.assignment.spring.data;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherRepository extends JpaRepository<WeatherEntity, UUID> {
    List<WeatherEntity> findByCity(String city);
}
