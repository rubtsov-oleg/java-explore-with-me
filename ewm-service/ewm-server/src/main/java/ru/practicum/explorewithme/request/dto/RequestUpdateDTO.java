package ru.practicum.explorewithme.request.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import ru.practicum.explorewithme.request.RequestStatus;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
public class RequestUpdateDTO {
    @NotNull(message = "requestIds can`t null!")
    private List<Integer> requestIds;
    @NotNull(message = "status can`t null!")
    private RequestStatus status;

    @JsonCreator
    public RequestUpdateDTO(
            @JsonProperty("requestIds") List<Integer> requestIds,
            @JsonProperty("status") RequestStatus status
    ) {
        this.requestIds = requestIds;
        this.status = status;
    }
}
