package ru.practicum.explorewithme.compilation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import ru.practicum.explorewithme.event.dto.EventShortDTO;

import java.util.List;

@Data
@Builder
public class CompilationOutDTO {
    private Integer id;
    private List<EventShortDTO> events;
    private Boolean pinned;
    private String title;

    @JsonCreator
    public CompilationOutDTO(
            @JsonProperty("id") Integer id,
            @JsonProperty("events") List<EventShortDTO> events,
            @JsonProperty("pinned") Boolean pinned,
            @JsonProperty("title") String title
    ) {
        this.id = id;
        this.events = events;
        this.pinned = pinned;
        this.title = title;
    }
}
