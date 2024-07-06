package ru.practicum.explorewithme.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.request.dto.RequestOutDTO;
import ru.practicum.explorewithme.request.dto.RequestUpdateDTO;
import ru.practicum.explorewithme.request.dto.RequestUpdateResultDTO;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping
public class RequestController {
    private final RequestService service;

    @PostMapping("/users/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public RequestOutDTO saveRequest(@RequestParam Integer eventId, @PathVariable Integer userId) {
        log.info("Получен запрос Post, на создание запроса.");
        return service.saveRequest(userId, eventId);
    }

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    public RequestOutDTO cancelRequest(@PathVariable Integer userId, @PathVariable Integer requestId) {
        log.info("Получен запрос Patch, на отмену запроса.");
        return service.cancelRequest(userId, requestId);
    }

    @GetMapping("/users/{userId}/requests")
    public List<RequestOutDTO> getAllRequestsByRequester(@PathVariable Integer userId) {
        log.info("Получен запрос GET, на получения всех запросов текущего пользователя.");
        List<RequestOutDTO> requestOutDTOList = service.getAllRequestsByRequester(userId);
        log.info("Получен ответ, список запросов текущего пользователя, размер: {}", requestOutDTOList.size());
        return requestOutDTOList;
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public List<RequestOutDTO> getAllRequestsByEvent(@PathVariable Integer userId, @PathVariable Integer eventId) {
        log.info("Получен запрос GET, на получения всех запросов по событию текущего пользователя.");
        List<RequestOutDTO> requestOutDTOList = service.getAllRequestsByEvent(userId, eventId);
        log.info("Получен ответ, список запросов по событию текущего пользователя, размер: {}",
                requestOutDTOList.size());
        return requestOutDTOList;
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    @Validated
    public RequestUpdateResultDTO updateRequests(@PathVariable Integer userId,
                                                 @PathVariable Integer eventId,
                                                 @Valid @RequestBody RequestUpdateDTO requestUpdateDTO) {
        log.info("Получен запрос Patch на изменение статуса заявок");
        return service.updateRequests(requestUpdateDTO, userId, eventId);
    }
}
