package ru.practicum.explorewithme.request.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import ru.practicum.explorewithme.request.RequestStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class RequestOutDTO {
    private Integer id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;
    private Integer event;
    private Integer requester;
    private RequestStatus status;

    @JsonCreator
    public RequestOutDTO(
            @JsonProperty("id") Integer id,
            @JsonProperty("created") LocalDateTime created,
            @JsonProperty("event") Integer event,
            @JsonProperty("requester") Integer requester,
            @JsonProperty("status") RequestStatus status
    ) {
        this.id = id;
        this.created = created;
        this.event = event;
        this.requester = requester;
        this.status = status;
    }
}
