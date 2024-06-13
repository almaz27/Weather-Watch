package io.bootify.weather_watcher.service;

import io.bootify.weather_watcher.domain.Alert;
import io.bootify.weather_watcher.domain.Sensor;
import io.bootify.weather_watcher.model.AlertDTO;
import io.bootify.weather_watcher.repos.AlertRepository;
import io.bootify.weather_watcher.repos.SensorRepository;
import io.bootify.weather_watcher.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class AlertService {

    private final AlertRepository alertRepository;
    private final SensorRepository sensorRepository;

    public AlertService(final AlertRepository alertRepository,
            final SensorRepository sensorRepository) {
        this.alertRepository = alertRepository;
        this.sensorRepository = sensorRepository;
    }

    public List<AlertDTO> findAll() {
        final List<Alert> alerts = alertRepository.findAll(Sort.by("id"));
        return alerts.stream()
                .map(alert -> mapToDTO(alert, new AlertDTO()))
                .toList();
    }

    public AlertDTO get(final Long id) {
        return alertRepository.findById(id)
                .map(alert -> mapToDTO(alert, new AlertDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final AlertDTO alertDTO) {
        final Alert alert = new Alert();
        mapToEntity(alertDTO, alert);
        return alertRepository.save(alert).getId();
    }

    public void update(final Long id, final AlertDTO alertDTO) {
        final Alert alert = alertRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(alertDTO, alert);
        alertRepository.save(alert);
    }

    public void delete(final Long id) {
        alertRepository.deleteById(id);
    }

    private AlertDTO mapToDTO(final Alert alert, final AlertDTO alertDTO) {
        alertDTO.setId(alert.getId());
        alertDTO.setEvent(alert.getEvent());
        alertDTO.setTime(alert.getTime());
        alertDTO.setSensor(alert.getSensor() == null ? null : alert.getSensor().getId());
        return alertDTO;
    }

    private Alert mapToEntity(final AlertDTO alertDTO, final Alert alert) {
        alert.setEvent(alertDTO.getEvent());
        alert.setTime(alertDTO.getTime());
        final Sensor sensor = alertDTO.getSensor() == null ? null : sensorRepository.findById(alertDTO.getSensor())
                .orElseThrow(() -> new NotFoundException("sensor not found"));
        alert.setSensor(sensor);
        return alert;
    }

}
