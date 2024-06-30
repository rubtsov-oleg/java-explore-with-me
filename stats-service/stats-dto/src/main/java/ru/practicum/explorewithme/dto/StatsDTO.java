package ru.practicum.explorewithme.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatsDTO {
    private String app;
    private String uri;
    private Long hits;

    @JsonCreator
    public StatsDTO(@JsonProperty("app") String app,
                    @JsonProperty("uri") String uri,
                    @JsonProperty("hits") Long hits) {
        this.app = app;
        this.uri = uri;
        this.hits = hits;
    }
}
