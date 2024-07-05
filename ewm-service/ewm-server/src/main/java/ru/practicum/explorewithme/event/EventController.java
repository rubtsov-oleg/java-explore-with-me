package ru.practicum.explorewithme.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.event.dto.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping
public class EventController {
    private final EventService service;

    @Validated
    @PostMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventOutDTO saveEvent(@Valid @RequestBody EventDTO eventDTO, @PathVariable Integer userId) {
        log.info("Получен запрос Post на создание события.");
        log.info("Добавлено событие: {}", eventDTO.getTitle());
        return service.saveEvent(eventDTO, userId);
    }

    @GetMapping("/events/{eventId}")
    public EventOutDTO getEventById(@PathVariable Integer eventId, HttpServletRequest request) {
        log.info("Получен запрос GET на получение события по id: {}", eventId);
        return service.getEventById(eventId, request);
    }

    @GetMapping("/events")
    public List<EventShortDTO> getAllEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Integer> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false, defaultValue = "EVENT_DATE") String sort,
            @RequestParam(defaultValue = "0") @Min(value = 0) Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size,
            HttpServletRequest request) {
        log.info("Получен запрос GET на получение всех событий.");
        return service.getAllEvents(
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request
        );
    }

    @GetMapping("/admin/events")
    public List<EventOutDTO> findAllEvents(@RequestParam(required = false) List<Integer> categories,
                                           @RequestParam(required = false) List<Integer> users,
                                           @RequestParam(required = false) List<String> states,
                                           @RequestParam(required = false) String rangeStart,
                                           @RequestParam(required = false) String rangeEnd,
                                           @RequestParam(defaultValue = "0") @Min(value = 0) Integer from,
                                           @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Получен запрос GET на поиск всех событий.");
        return service.findAllEvents(states, users, categories, rangeStart, rangeEnd, from, size);
    }

    @Validated
    @PatchMapping("/admin/events/{eventId}")
    public EventOutDTO updateEventByAdmin(@PathVariable Integer eventId,
                                          @Valid @RequestBody EventAdminUpdateDTO eventAdminUpdateDTO) {
        log.info("Получен запрос Patch на изменение события администратором.");
        return service.updateEventByAdmin(eventAdminUpdateDTO, eventId);
    }

    @GetMapping("/users/{userId}/events")
    public List<EventShortDTO> getEventsByUserId(@PathVariable Integer userId,
                                                 @RequestParam(defaultValue = "0") @Min(value = 0) Integer from,
                                                 @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Получен запрос Get на получение событий, добавленным текущим пользователем.");
        return service.getEventsByUserId(userId, from, size);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventOutDTO getEventById(@PathVariable Integer eventId, @PathVariable Integer userId) {
        log.info("Получен запрос GET на получение события по id: {}", eventId);
        return service.getEventById(eventId, userId);
    }

    @Validated
    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventOutDTO updateEventByUser(@PathVariable Integer eventId,
                                         @Valid @RequestBody EventUserUpdateDTO eventUserUpdateDTO,
                                         @PathVariable Integer userId) {
        log.info("Получен запрос Patch на изменение события пользователем.");
        return service.updateEventByUser(eventUserUpdateDTO, eventId, userId);
    }
}
