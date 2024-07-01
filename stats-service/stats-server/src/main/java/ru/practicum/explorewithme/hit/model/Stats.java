package ru.practicum.explorewithme.hit.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Stats {
    private String app;
    private String uri;
    private Long hits;

    public Stats(String app, String uri, Long hits) {
        this.app = app;
        this.uri = uri;
        this.hits = hits;
    }
}
