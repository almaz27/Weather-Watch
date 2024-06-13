package io.bootify.weather_watcher.model;

import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AlertDTO {

    private Long id;

    @Size(max = 255)
    private String event;

    private LocalDateTime time;

    private Long sensor;

}
