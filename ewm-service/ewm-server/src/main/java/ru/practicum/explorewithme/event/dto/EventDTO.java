package ru.practicum.explorewithme.event.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@Builder
public class EventDTO {
    private Integer id;

    @NotNull(message = "Annotation can`t be null!")
    @NotBlank(message = "Annotation can`t be blank!")
    @Size(min = 20, max = 2000, message = "Min size 20, Max size 2000!")
    private String annotation;

    @NotNull(message = "Category can`t be null!")
    private Integer category;

    @NotNull(message = "Description can`t be null!")
    @NotBlank(message = "Description can`t be blank!")
    @Size(min = 20, max = 7000, message = "Min size 20, Max size 7000!")
    private String description;

    @NotNull(message = "EventDate can`t null!")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @NotNull(message = "Location can`t null!")
    private LocationDTO location;

    private Boolean paid;

    @Min(value = 0, message = "ParticipantLimit must be greater than 0")
    private Integer participantLimit;

    private Boolean requestModeration;

    @NotNull(message = "Title can`t be null!")
    @NotBlank(message = "Title can`t be blank!")
    @Size(min = 3, max = 120, message = "Min size 3, Max size 120!")
    private String title;

    @JsonCreator
    public EventDTO(
            @JsonProperty("id") Integer id,
            @JsonProperty("annotation") String annotation,
            @JsonProperty("category") Integer category,
            @JsonProperty("description") String description,
            @JsonProperty("eventDate") LocalDateTime eventDate,
            @JsonProperty("location") LocationDTO location,
            @JsonProperty("paid") Boolean paid,
            @JsonProperty("participantLimit") Integer participantLimit,
            @JsonProperty("requestModeration") Boolean requestModeration,
            @JsonProperty("title") String title) {
        this.id = id;
        this.annotation = annotation;
        this.category = category;
        this.description = description;
        this.eventDate = eventDate;
        this.location = location;
        this.paid = paid;
        this.participantLimit = participantLimit;
        this.requestModeration = requestModeration;
        this.title = title;
    }

    @Data
    @Builder
    public static class LocationDTO {
        @NotNull(message = "Lat can`t be null!")
        private double lat;

        @NotNull(message = "Lon can`t be null!")
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
