package io.bootify.weather_watcher.rest;

import io.bootify.weather_watcher.model.SensorDTO;
import io.bootify.weather_watcher.service.SensorService;
import io.bootify.weather_watcher.util.ReferencedException;
import io.bootify.weather_watcher.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/sensors", produces = MediaType.APPLICATION_JSON_VALUE)
public class SensorResource {

    private final SensorService sensorService;

    public SensorResource(final SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @GetMapping
    public ResponseEntity<List<SensorDTO>> getAllSensors() {
        return ResponseEntity.ok(sensorService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SensorDTO> getSensor(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(sensorService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createSensor(@RequestBody @Valid final SensorDTO sensorDTO) {
        final Long createdId = sensorService.create(sensorDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateSensor(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final SensorDTO sensorDTO) {
        sensorService.update(id, sensorDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteSensor(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = sensorService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        sensorService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
