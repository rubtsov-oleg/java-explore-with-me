package ru.practicum.explorewithme.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
public class HitDTO {
    private Integer id;
    @NotNull(message = "app can`t null!")
    @Size(min = 1, max = 200, message = "Max size 200!")
    private String app;
    @NotNull(message = "uri can`t null!")
    @Size(min = 1, max = 200, message = "Max size 200!")
    private String uri;
    @NotNull(message = "up can`t null!")
    @Size(min = 1, max = 200, message = "Max size 200!")
    private String ip;
    @NotNull(message = "timestamp can`t null!")
    @Past(message = "timestamp can`t be in feature!")
    private LocalDateTime timestamp;
}