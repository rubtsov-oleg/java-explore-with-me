package ru.practicum.explorewithme.comment.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class CommentDTO {
    @NotBlank(message = "Text can`t be null!")
    private String text;

    @JsonCreator
    public CommentDTO(@JsonProperty("text") String text) {
        this.text = text;
    }
}
