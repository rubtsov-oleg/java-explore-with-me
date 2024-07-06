package ru.practicum.explorewithme.event.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import ru.practicum.explorewithme.event.StateActionAdmin;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
public class EventAdminUpdateDTO {
    @Size(min = 20, max = 2000, message = "Min size 20, Max size 2000!")
    private String annotation;

    private Integer category;

    @Size(min = 20, max = 7000, message = "Min size 20, Max size 7000!")
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private LocationDTO location;

    private Boolean paid;

    @Min(value = 0, message = "ParticipantLimit must be greater than 0")
    private Integer participantLimit;

    private Boolean requestModeration;

    private StateActionAdmin stateAction;

    @Size(min = 3, max = 120, message = "Min size 3, Max size 120!")
    private String title;

    @JsonCreator
    public EventAdminUpdateDTO(
            @JsonProperty("annotation") String annotation,
            @JsonProperty("category") Integer category,
            @JsonProperty("description") String description,
            @JsonProperty("eventDate") LocalDateTime eventDate,
            @JsonProperty("location") LocationDTO location,
            @JsonProperty("paid") Boolean paid,
            @JsonProperty("participantLimit") Integer participantLimit,
            @JsonProperty("requestModeration") Boolean requestModeration,
            @JsonProperty("stateAction") StateActionAdmin stateAction,
            @JsonProperty("title") String title) {
        this.annotation = annotation;
        this.category = category;
        this.description = description;
        this.eventDate = eventDate;
        this.location = location;
        this.paid = paid;
        this.participantLimit = participantLimit;
        this.requestModeration = requestModeration;
        this.stateAction = stateAction;
        this.title = title;
    }

    @Data
    @Builder
    public static class LocationDTO {
        private double lat;
        private double lon;

        @JsonCreator
        public LocationDTO(
                @JsonProperty("lat") double lat,
                @JsonProperty("lon") double lon) {
            this.lat = lat;
            this.lon = lon;
        }
    }
}