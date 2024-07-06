package ru.practicum.explorewithme.request;

import ru.practicum.explorewithme.request.dto.RequestOutDTO;
import ru.practicum.explorewithme.request.dto.RequestUpdateDTO;
import ru.practicum.explorewithme.request.dto.RequestUpdateResultDTO;

import java.util.List;

public interface RequestService {
    RequestOutDTO saveRequest(Integer userId, Integer eventId);

    RequestOutDTO cancelRequest(Integer userId, Integer requestId);

    List<RequestOutDTO> getAllRequestsByRequester(Integer userId);

    List<RequestOutDTO> getAllRequestsByEvent(Integer userId, Integer eventId);

    RequestUpdateResultDTO updateRequests(RequestUpdateDTO requestUpdateDTO, Integer userId, Integer eventId);
}
