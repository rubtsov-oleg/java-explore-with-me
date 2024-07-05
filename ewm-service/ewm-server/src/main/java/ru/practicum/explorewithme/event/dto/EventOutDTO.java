package ru.practicum.explorewithme.event.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import ru.practicum.explorewithme.category.CategoryDTO;
import ru.practicum.explorewithme.event.model.EventState;
import ru.practicum.explorewithme.user.UserShortDTO;

import java.time.LocalDateTime;

@Data
@Builder
public class EventOutDTO {
    private Integer id;
    private String annotation;
    private CategoryDTO category;
    private Integer confirmedRequests;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private UserShortDTO initiator;
    private LocationOutDTO location;
    private Boolean paid;
    private Integer participantLimit;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private EventState state;
    private String title;
    private Long views;

    @JsonCreator
    public EventOutDTO(
            @JsonProperty("id") Integer id,
            @JsonProperty("annotation") String annotation,
            @JsonProperty("category") CategoryDTO category,
            @JsonProperty("confirmedRequests") Integer confirmedRequests,
            @JsonProperty("createdOn") LocalDateTime createdOn,
            @JsonProperty("description") String description,
            @JsonProperty("eventDate") LocalDateTime eventDate,
            @JsonProperty("initiator") UserShortDTO initiator,
            @JsonProperty("location") LocationOutDTO location,
            @JsonProperty("paid") Boolean paid,
            @JsonProperty("participantLimit") Integer participantLimit,
            @JsonProperty("publishedOn") LocalDateTime publishedOn,
            @JsonProperty("requestModeration") Boolean requestModeration,
            @JsonProperty("state") EventState state,
            @JsonProperty("title") String title,
            @JsonProperty("views") Long views) {
        this.id = id;
        this.annotation = annotation;
        this.category = category;
        this.confirmedRequests = confirmedRequests;
        this.createdOn = createdOn;
        this.description = description;
        this.eventDate = eventDate;
        this.initiator = initiator;
        this.location = location;
        this.paid = paid;
        this.participantLimit = participantLimit;
        this.publishedOn = publishedOn;
        this.requestModeration = requestModeration;
        this.state = state;
        this.title = title;
        this.views = views;
    }

    @Data
    @Builder
    public static class LocationOutDTO {
        private double lat;
        private double lon;

        @JsonCreator
        public LocationOutDTO(
                @JsonProperty("lat") double lat,
                @JsonProperty("lon") double lon) {
            this.lat = lat;
            this.lon = lon;
        }
    }
}
