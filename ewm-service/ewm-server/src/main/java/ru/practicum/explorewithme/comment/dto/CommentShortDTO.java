package ru.practicum.explorewithme.comment.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import ru.practicum.explorewithme.user.UserShortDTO;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentShortDTO {
    private Integer id;
    private String text;
    private UserShortDTO author;
    private LocalDateTime created;
    private LocalDateTime updated;

    @JsonCreator
    public CommentShortDTO(
            @JsonProperty("id") Integer id,
            @JsonProperty("text") String text,
            @JsonProperty("author") UserShortDTO author,
            @JsonProperty("created") LocalDateTime created,
            @JsonProperty("updated") LocalDateTime updated) {
        this.id = id;
        this.text = text;
        this.author = author;
        this.created = created;
        this.updated = updated;
    }
}