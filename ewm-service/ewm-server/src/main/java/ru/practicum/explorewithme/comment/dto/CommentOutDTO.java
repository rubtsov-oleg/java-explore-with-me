package ru.practicum.explorewithme.comment.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentOutDTO {
    private Integer id;
    private String text;
    private Integer event;
    private Integer author;
    private LocalDateTime created;
    private LocalDateTime updated;

    @JsonCreator
    public CommentOutDTO(
            @JsonProperty("id") Integer id,
            @JsonProperty("text") String text,
            @JsonProperty("event") Integer event,
            @JsonProperty("author") Integer author,
            @JsonProperty("created") LocalDateTime created,
            @JsonProperty("updated") LocalDateTime updated) {
        this.id = id;
        this.text = text;
        this.event = event;
        this.author = author;
        this.created = created;
        this.updated = updated;
    }
}
