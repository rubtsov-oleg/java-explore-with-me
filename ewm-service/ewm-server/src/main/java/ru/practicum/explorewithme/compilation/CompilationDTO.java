package ru.practicum.explorewithme.compilation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import ru.practicum.explorewithme.anotation.MarkerOfCreate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
public class CompilationDTO {
    private List<Integer> events;
    private Boolean pinned;

    @NotBlank(groups = MarkerOfCreate.class, message = "Title can`t be blank!")
    @Size(min = 1, max = 50, message = "Min size 1, Max size 50!")
    private String title;

    @JsonCreator
    public CompilationDTO(
            @JsonProperty("events") List<Integer> events,
            @JsonProperty("pinned") Boolean pinned,
            @JsonProperty("title") String title
    ) {
        this.events = events;
        this.pinned = pinned;
        this.title = title;
    }
}
