package ru.practicum.explorewithme.event;

import ru.practicum.explorewithme.event.dto.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {
    EventOutDTO saveEvent(EventDTO eventDTO, Integer userId);

    EventOutDTO getEventById(Integer eventId, HttpServletRequest request);

    List<EventShortDTO> getAllEvents(String text, List<Integer> categories, Boolean paid, String rangeStart,
                                     String rangeEnd, Boolean onlyAvailable, String sort, Integer from,
                                     Integer size, HttpServletRequest request);

    List<EventOutDTO> findAllEvents(List<String> states, List<Integer> users, List<Integer> categories,
                                    String rangeStart, String rangeEnd, Integer from, Integer size);

    EventOutDTO updateEventByAdmin(EventAdminUpdateDTO eventAdminUpdateDTO, Integer eventId);

    List<EventShortDTO> getEventsByUserId(Integer userId, Integer from, Integer size);

    EventOutDTO getEventById(Integer eventId, Integer userId);

    EventOutDTO updateEventByUser(EventUserUpdateDTO eventUserUpdateDTO, Integer eventId, Integer userId);
}
