package ru.practicum.explorewithme.request.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RequestUpdateResultDTO {
    private List<RequestOutDTO> confirmedRequests;
    private List<RequestOutDTO> rejectedRequests;

    @JsonCreator
    public RequestUpdateResultDTO(
            @JsonProperty("confirmedRequests") List<RequestOutDTO> confirmedRequests,
            @JsonProperty("rejectedRequests") List<RequestOutDTO> rejectedRequests
    ) {
        this.confirmedRequests = confirmedRequests;
        this.rejectedRequests = rejectedRequests;
    }
}
