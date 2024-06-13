package io.bootify.weather_watcher.rest;

import io.bootify.weather_watcher.model.DataDTO;
import io.bootify.weather_watcher.service.DataService;
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
@RequestMapping(value = "/api/datas", produces = MediaType.APPLICATION_JSON_VALUE)
public class DataResource {

    private final DataService dataService;

    public DataResource(final DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping
    public ResponseEntity<List<DataDTO>> getAllDatas() {
        return ResponseEntity.ok(dataService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataDTO> getData(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(dataService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createData(@RequestBody @Valid final DataDTO dataDTO) {
        final Long createdId = dataService.create(dataDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateData(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final DataDTO dataDTO) {
        dataService.update(id, dataDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteData(@PathVariable(name = "id") final Long id) {
        dataService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
