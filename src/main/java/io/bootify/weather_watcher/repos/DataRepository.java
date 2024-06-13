package io.bootify.weather_watcher.repos;

import io.bootify.weather_watcher.domain.Data;
import io.bootify.weather_watcher.domain.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DataRepository extends JpaRepository<Data, Long> {

    Data findFirstBySensor(Sensor sensor);

}
