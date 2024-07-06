package ru.practicum.explorewithme.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.category.CategoryRepository;
import ru.practicum.explorewithme.client.StatsClient;
import ru.practicum.explorewithme.dto.HitDTO;
import ru.practicum.explorewithme.event.dto.*;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.model.EventState;
import ru.practicum.explorewithme.exception.ValidationException;
import ru.practicum.explorewithme.request.RequestRepository;
import ru.practicum.explorewithme.user.User;
import ru.practicum.explorewithme.user.UserRepository;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventMapper eventMapper;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    @Value("${stats-server.url}")
    private String statsServerUrl;
    private StatsClient statsClient;

    @PostConstruct
    public void init() {
        this.statsClient = new StatsClient(statsServerUrl);
    }

    @Override
    @Transactional
    public EventOutDTO saveEvent(EventDTO eventDTO, Integer userId) {
        Event event = eventMapper.toModel(eventDTO, categoryRepository);
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidationException("Неверная дата");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Пользователь " + userId + " не найден"));
        event.setInitiator(user);
        event.setState(EventState.PENDING);
        event.setCreatedOn(LocalDateTime.now());
        return eventMapper.toOutDTO(eventRepository.save(event), statsClient, requestRepository);
    }

    @Override
    public EventOutDTO getEventById(Integer eventId, HttpServletRequest request) {
        saveStats(request);
        EventOutDTO eventOutDTO = eventMapper.toOutDTO(eventRepository.findById(eventId)
                        .orElseThrow(() -> new NoSuchElementException("Событие " + eventId + " не найдено")),
                statsClient, requestRepository);
        if (!eventOutDTO.getState().equals(EventState.PUBLISHED)) {
            throw new NoSuchElementException("Событие " + eventId + " не найдено");
        }
        return eventOutDTO;
    }

    @Override
    public List<EventShortDTO> getAllEvents(String text, List<Integer> categories, Boolean paid, String rangeStart,
                                            String rangeEnd, Boolean onlyAvailable, String sort, Integer from,
                                            Integer size, HttpServletRequest request) {
        if (categories != null) {
            if (categories.stream().anyMatch((category -> category <= 0))) {
                throw new ValidationException("Переданы некорректные категории");
            }
        }
        Pageable pageable = PageRequest.of(from / size, size);
        List<Event> eventList = eventRepository.getAllEvents(
                text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable != null && onlyAvailable, pageable
        ).getContent();
        List<EventShortDTO> eventShortDTOList = eventMapper.toShortDTOList(eventList, statsClient, requestRepository)
                .stream().sorted(getComparator(sort)).collect(Collectors.toList());
        saveStats(request);
        return eventShortDTOList;
    }

    @Override
    public List<EventOutDTO> findAllEvents(List<String> states, List<Integer> users, List<Integer> categories,
                                           String rangeStart, String rangeEnd, Integer from, Integer size) {
        return eventMapper.toOutDTOList(
                eventRepository.findAllEvents(
                                states, users, categories, rangeStart,
                                rangeEnd, PageRequest.of(from / size, size))
                        .getContent(), statsClient, requestRepository
        );
    }

    @Override
    @Transactional
    public EventOutDTO updateEventByAdmin(EventAdminUpdateDTO eventAdminUpdateDTO, Integer eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NoSuchElementException("Событие " + eventId + " не найдено"));
        if (eventAdminUpdateDTO.getEventDate() != null
                && LocalDateTime.now().plusHours(1).isAfter(eventAdminUpdateDTO.getEventDate())) {
            throw new ValidationException("Неверная дата");
        }
        if (eventAdminUpdateDTO.getEventDate() != null) {
            event.setEventDate(eventAdminUpdateDTO.getEventDate());
        }
        if (eventAdminUpdateDTO.getStateAction() != null) {
            if (eventAdminUpdateDTO.getStateAction().equals(StateActionAdmin.PUBLISH_EVENT)
                    && !event.getState().equals(EventState.PENDING)) {
                throw new DataIntegrityViolationException("Невозможно опубликовать событие");
            }
            if (eventAdminUpdateDTO.getStateAction().equals(StateActionAdmin.REJECT_EVENT)
                    && event.getState().equals(EventState.PUBLISHED)) {
                throw new DataIntegrityViolationException("Невозможно отклонить событие");
            }
            if (eventAdminUpdateDTO.getStateAction().equals(StateActionAdmin.PUBLISH_EVENT)) {
                event.setState(EventState.PUBLISHED);
            } else if (eventAdminUpdateDTO.getStateAction().equals(StateActionAdmin.REJECT_EVENT)) {
                event.setState(EventState.CANCELED);
            }
        }
        if (eventAdminUpdateDTO.getAnnotation() != null) {
            event.setAnnotation(eventAdminUpdateDTO.getAnnotation());
        }
        if (eventAdminUpdateDTO.getCategory() != null) {
            event.setCategory(categoryRepository.findById(eventAdminUpdateDTO.getCategory())
                    .orElseThrow(() -> new NoSuchElementException("Катигория не найдена")));
        }
        if (eventAdminUpdateDTO.getDescription() != null) {
            event.setDescription(eventAdminUpdateDTO.getDescription());
        }
        if (eventAdminUpdateDTO.getLocation() != null) {
            Event.Location location = new Event.Location();
            location.setLat(eventAdminUpdateDTO.getLocation().getLat());
            location.setLon(eventAdminUpdateDTO.getLocation().getLon());
            event.setLocation(location);
        }
        if (eventAdminUpdateDTO.getPaid() != null) {
            event.setPaid(eventAdminUpdateDTO.getPaid());
        }
        if (eventAdminUpdateDTO.getParticipantLimit() != null) {
            event.setParticipantLimit(eventAdminUpdateDTO.getParticipantLimit());
        }
        if (eventAdminUpdateDTO.getRequestModeration() != null) {
            event.setRequestModeration(eventAdminUpdateDTO.getRequestModeration());
        }
        if (eventAdminUpdateDTO.getTitle() != null) {
            event.setTitle(eventAdminUpdateDTO.getTitle());
        }
        return eventMapper.toOutDTO(eventRepository.save(event), statsClient, requestRepository);
    }

    @Override
    public List<EventShortDTO> getEventsByUserId(Integer userId, Integer from, Integer size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Пользователь " + userId + " не найден"));
        return eventMapper.toShortDTOList(
                eventRepository.findAllByInitiator(user, PageRequest.of(from / size, size)).getContent(),
                statsClient, requestRepository);
    }

    @Override
    public EventOutDTO getEventById(Integer eventId, Integer userId) {
        EventOutDTO eventOutDTO = eventMapper.toOutDTO(eventRepository.findById(eventId)
                        .orElseThrow(() -> new NoSuchElementException("Событие " + eventId + " не найдено")),
                statsClient, requestRepository);
        if (!eventOutDTO.getInitiator().getId().equals(userId)) {
            throw new NoSuchElementException("Событие " + eventId + " не найдено");
        }
        return eventOutDTO;
    }

    @Override
    @Transactional
    public EventOutDTO updateEventByUser(EventUserUpdateDTO eventUserUpdateDTO, Integer eventId, Integer userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NoSuchElementException("Событие " + eventId + " не найдено"));
        if (!event.getInitiator().getId().equals(userId)) {
            throw new NoSuchElementException("Событие " + eventId + " не найдено");
        }
        if (eventUserUpdateDTO.getEventDate() != null
                && LocalDateTime.now().plusHours(2).isAfter(eventUserUpdateDTO.getEventDate())) {
            throw new ValidationException("Неверная дата");
        }
        if (eventUserUpdateDTO.getEventDate() != null) {
            event.setEventDate(eventUserUpdateDTO.getEventDate());
        }
        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new DataIntegrityViolationException("Невозможно изменять опубликованные события");
        }
        if (eventUserUpdateDTO.getStateAction() != null) {
            if (eventUserUpdateDTO.getStateAction().equals(StateActionUser.SEND_TO_REVIEW)) {
                event.setState(EventState.PENDING);
            } else if (eventUserUpdateDTO.getStateAction().equals(StateActionUser.CANCEL_REVIEW)) {
                event.setState(EventState.CANCELED);
            }
        }
        if (eventUserUpdateDTO.getAnnotation() != null) {
            event.setAnnotation(eventUserUpdateDTO.getAnnotation());
        }
        if (eventUserUpdateDTO.getCategory() != null) {
            event.setCategory(categoryRepository.findById(eventUserUpdateDTO.getCategory())
                    .orElseThrow(() -> new NoSuchElementException("Катигория не найдена")));
        }
        if (eventUserUpdateDTO.getDescription() != null) {
            event.setDescription(eventUserUpdateDTO.getDescription());
        }
        if (eventUserUpdateDTO.getLocation() != null) {
            Event.Location location = new Event.Location();
            location.setLat(eventUserUpdateDTO.getLocation().getLat());
            location.setLon(eventUserUpdateDTO.getLocation().getLon());
            event.setLocation(location);
        }
        if (eventUserUpdateDTO.getPaid() != null) {
            event.setPaid(eventUserUpdateDTO.getPaid());
        }
        if (eventUserUpdateDTO.getParticipantLimit() != null) {
            event.setParticipantLimit(eventUserUpdateDTO.getParticipantLimit());
        }
        if (eventUserUpdateDTO.getRequestModeration() != null) {
            event.setRequestModeration(eventUserUpdateDTO.getRequestModeration());
        }
        if (eventUserUpdateDTO.getTitle() != null) {
            event.setTitle(eventUserUpdateDTO.getTitle());
        }
        return eventMapper.toOutDTO(eventRepository.save(event), statsClient, requestRepository);
    }

    private Comparator<EventShortDTO> getComparator(String sort) {
        if ("EVENT_DATE".equals(sort)) {
            return Comparator.comparing(EventShortDTO::getEventDate);
        } else if ("VIEWS".equals(sort)) {
            return Comparator.comparing(EventShortDTO::getViews).reversed();
        } else {
            throw new ValidationException("Некорректный параметр сортировки");
        }
    }

    private void saveStats(HttpServletRequest request) {
        HitDTO hitDTO = HitDTO.builder()
                .uri(request.getRequestURI())
                .app("explore-with-me")
                .timestamp(LocalDateTime.now())
                .ip(request.getRemoteAddr())
                .build();
        statsClient.saveHit(hitDTO);
    }
}
