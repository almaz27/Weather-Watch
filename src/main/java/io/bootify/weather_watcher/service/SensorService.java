package io.bootify.weather_watcher.service;

import io.bootify.weather_watcher.domain.Alert;
import io.bootify.weather_watcher.domain.Data;
import io.bootify.weather_watcher.domain.Sensor;
import io.bootify.weather_watcher.model.SensorDTO;
import io.bootify.weather_watcher.repos.AlertRepository;
import io.bootify.weather_watcher.repos.DataRepository;
import io.bootify.weather_watcher.repos.SensorRepository;
import io.bootify.weather_watcher.util.NotFoundException;
import io.bootify.weather_watcher.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class SensorService {

    private final SensorRepository sensorRepository;
    private final DataRepository dataRepository;
    private final AlertRepository alertRepository;

    public SensorService(final SensorRepository sensorRepository,
            final DataRepository dataRepository, final AlertRepository alertRepository) {
        this.sensorRepository = sensorRepository;
        this.dataRepository = dataRepository;
        this.alertRepository = alertRepository;
    }

    public List<SensorDTO> findAll() {
        final List<Sensor> sensors = sensorRepository.findAll(Sort.by("id"));
        return sensors.stream()
                .map(sensor -> mapToDTO(sensor, new SensorDTO()))
                .toList();
    }

    public SensorDTO get(final Long id) {
        return sensorRepository.findById(id)
                .map(sensor -> mapToDTO(sensor, new SensorDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final SensorDTO sensorDTO) {
        final Sensor sensor = new Sensor();
        mapToEntity(sensorDTO, sensor);
        return sensorRepository.save(sensor).getId();
    }

    public void update(final Long id, final SensorDTO sensorDTO) {
        final Sensor sensor = sensorRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(sensorDTO, sensor);
        sensorRepository.save(sensor);
    }

    public void delete(final Long id) {
        sensorRepository.deleteById(id);
    }

    private SensorDTO mapToDTO(final Sensor sensor, final SensorDTO sensorDTO) {
        sensorDTO.setId(sensor.getId());
        sensorDTO.setName(sensor.getName());
        sensorDTO.setType(sensor.getType());
        sensorDTO.setModel(sensor.getModel());
        sensorDTO.setInstalationDate(sensor.getInstalationDate());
        sensorDTO.setStatus(sensor.getStatus());
        return sensorDTO;
    }

    private Sensor mapToEntity(final SensorDTO sensorDTO, final Sensor sensor) {
        sensor.setName(sensorDTO.getName());
        sensor.setType(sensorDTO.getType());
        sensor.setModel(sensorDTO.getModel());
        sensor.setInstalationDate(sensorDTO.getInstalationDate());
        sensor.setStatus(sensorDTO.getStatus());
        return sensor;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Sensor sensor = sensorRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Data sensorData = dataRepository.findFirstBySensor(sensor);
        if (sensorData != null) {
            referencedWarning.setKey("sensor.data.sensor.referenced");
            referencedWarning.addParam(sensorData.getId());
            return referencedWarning;
        }
        final Alert sensorAlert = alertRepository.findFirstBySensor(sensor);
        if (sensorAlert != null) {
            referencedWarning.setKey("sensor.alert.sensor.referenced");
            referencedWarning.addParam(sensorAlert.getId());
            return referencedWarning;
        }
        return null;
    }

}
