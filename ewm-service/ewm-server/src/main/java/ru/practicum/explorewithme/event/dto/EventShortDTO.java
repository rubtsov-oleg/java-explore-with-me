package ru.practicum.explorewithme.event.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import ru.practicum.explorewithme.category.CategoryDTO;
import ru.practicum.explorewithme.user.UserShortDTO;

import java.time.LocalDateTime;

@Data
@Builder
public class EventShortDTO implements Comparable<EventShortDTO> {
    private Integer id;
    private String annotation;
    private CategoryDTO category;
    private Integer confirmedRequests;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private UserShortDTO initiator;
    private Boolean paid;
    private String title;
    private Long views;

    @JsonCreator
    public EventShortDTO(
            @JsonProperty("id") Integer id,
            @JsonProperty("annotation") String annotation,
            @JsonProperty("category") CategoryDTO category,
            @JsonProperty("confirmedRequests") Integer confirmedRequests,
            @JsonProperty("eventDate") LocalDateTime eventDate,
            @JsonProperty("initiator") UserShortDTO initiator,
            @JsonProperty("paid") Boolean paid,
            @JsonProperty("title") String title,
            @JsonProperty("views") Long views) {
        this.id = id;
        this.annotation = annotation;
        this.category = category;
        this.confirmedRequests = confirmedRequests;
        this.eventDate = eventDate;
        this.initiator = initiator;
        this.paid = paid;
        this.title = title;
        this.views = views;
    }

    @Override
    public int compareTo(EventShortDTO other) {
        return this.id.compareTo(other.id);
    }
}
