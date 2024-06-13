package io.bootify.weather_watcher.repos;

import io.bootify.weather_watcher.domain.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SensorRepository extends JpaRepository<Sensor, Long> {
}
