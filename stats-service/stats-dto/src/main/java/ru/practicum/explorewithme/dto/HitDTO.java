package ru.practicum.explorewithme.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@Builder
public class HitDTO {
    private Integer id;
    @NotNull(message = "app can`t null!")
    @Size(min = 1, max = 30, message = "Max size 30!")
    private String app;
    @NotNull(message = "uri can`t null!")
    @Size(min = 1, max = 2000, message = "Max size 2000!")
    private String uri;
    @NotNull(message = "ip can`t null!")
    @Size(min = 1, max = 15, message = "Max size 15!")
    private String ip;
    @NotNull(message = "timestamp can`t null!")
    @PastOrPresent(message = "timestamp can`t be in feature!")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    @JsonCreator
    public HitDTO(@JsonProperty("id") Integer id,
                  @JsonProperty("app") String app,
                  @JsonProperty("uri") String uri,
                  @JsonProperty("ip") String ip,
                  @JsonProperty("timestamp") LocalDateTime timestamp) {
        this.id = id;
        this.app = app;
        this.uri = uri;
        this.ip = ip;
        this.timestamp = timestamp;
    }
}