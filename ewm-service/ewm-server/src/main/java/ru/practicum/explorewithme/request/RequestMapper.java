package ru.practicum.explorewithme.request;

import org.mapstruct.Mapper;
import ru.practicum.explorewithme.request.dto.RequestOutDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RequestMapper {
    RequestOutDTO toOutDTO(Request request);

    List<RequestOutDTO> toOutDTOList(List<Request> requests);
}
