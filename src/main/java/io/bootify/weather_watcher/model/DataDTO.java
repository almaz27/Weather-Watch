package io.bootify.weather_watcher.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class DataDTO {

    private Long id;

    @NotNull
    private Double temperature;

    private Integer humidity;

    private Integer windSpeed;

    @Size(max = 255)
    private String parameter;

    private Long sensor;

}
