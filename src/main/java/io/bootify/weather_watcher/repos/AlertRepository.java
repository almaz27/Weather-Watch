package io.bootify.weather_watcher.repos;

import io.bootify.weather_watcher.domain.Alert;
import io.bootify.weather_watcher.domain.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AlertRepository extends JpaRepository<Alert, Long> {

    Alert findFirstBySensor(Sensor sensor);

}
