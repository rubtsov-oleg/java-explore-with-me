package ru.practicum.explorewithme.request;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.event.EventRepository;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.model.EventState;
import ru.practicum.explorewithme.exception.ValidationException;
import ru.practicum.explorewithme.request.dto.RequestOutDTO;
import ru.practicum.explorewithme.request.dto.RequestUpdateDTO;
import ru.practicum.explorewithme.request.dto.RequestUpdateResultDTO;
import ru.practicum.explorewithme.user.User;
import ru.practicum.explorewithme.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public RequestOutDTO saveRequest(Integer userId, Integer eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NoSuchElementException("Событие " + eventId + " не найдено"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Пользователь " + userId + " не найден"));
        if (requestRepository.findByRequesterAndEvent(user.getId(), event.getId()).isPresent()) {
            throw new DataIntegrityViolationException("Нельзя отправлять заявку повторно");
        }
        if (event.getInitiator().equals(user)) {
            throw new DataIntegrityViolationException("Нельзя подавать заявку на своё событие");
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new DataIntegrityViolationException("Нельзя участвовать в неопубликованном событии");
        }
        if ((event.getParticipantLimit() != 0) && (requestRepository.countByEventIdAndStatusConfirmed(
                event.getId()) >= event.getParticipantLimit())) {
            throw new DataIntegrityViolationException("Достигнут лимит запросов на участие");
        }
        Request request = new Request();
        request.setRequester(userId);
        request.setCreated(LocalDateTime.now());
        request.setEvent(eventId);
        if (!event.isRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(RequestStatus.CONFIRMED);
        } else {
            request.setStatus(RequestStatus.PENDING);
        }
        return requestMapper.toOutDTO(requestRepository.save(request));
    }

    @Override
    @Transactional
    public RequestOutDTO cancelRequest(Integer userId, Integer requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NoSuchElementException("Запрос " + requestId + " не найден"));
        if (!request.getRequester().equals(userId)) {
            throw new ValidationException("Пользователь " + userId + "не подавал эту заявку");
        }
        request.setStatus(RequestStatus.CANCELED);
        return requestMapper.toOutDTO(requestRepository.save(request));
    }

    @Override
    public List<RequestOutDTO> getAllRequestsByRequester(Integer userId) {
        return requestMapper.toOutDTOList(requestRepository.findAllByRequester(userId));
    }

    @Override
    public List<RequestOutDTO> getAllRequestsByEvent(Integer userId, Integer eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NoSuchElementException("Событие " + eventId + " не найдено"));
        if (!event.getInitiator().getId().equals(userId)) {
            throw new DataIntegrityViolationException("Данный пользователь не является инициатором события");
        }
        return requestMapper.toOutDTOList(requestRepository.findAllByEvent(eventId));
    }

    @Override
    @Transactional
    public RequestUpdateResultDTO updateRequests(RequestUpdateDTO requestUpdateDTO, Integer userId, Integer eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NoSuchElementException("Событие " + eventId + " не найдено"));
        if (!event.getInitiator().getId().equals(userId)) {
            throw new DataIntegrityViolationException("Данный пользователь не является инициатором события");
        }
        List<Request> requests = requestRepository.findAllByIdIn(requestUpdateDTO.getRequestIds());
        if (requests.size() != requestUpdateDTO.getRequestIds().size()) {
            throw new DataIntegrityViolationException("Не все запросы были найдены");
        }
        for (Request request : requests) {
            if (!request.getEvent().equals(eventId)) {
                throw new DataIntegrityViolationException("Не все запросы относятся к данному событию");
            }
            if (!request.getStatus().equals(RequestStatus.PENDING)) {
                throw new DataIntegrityViolationException("Запросы должны быть в статусе PENDING");
            }
        }

        long confirmedRequestCount = requestRepository.countByEventIdAndStatusConfirmed(eventId);

        if (event.getParticipantLimit() != 0 && confirmedRequestCount >= event.getParticipantLimit()) {
            throw new DataIntegrityViolationException("Достигнут лимит");
        }

        if (event.getParticipantLimit() == 0 || requestUpdateDTO.getStatus().equals(RequestStatus.CANCELED)
                || requestUpdateDTO.getStatus().equals(RequestStatus.REJECTED)) {
            for (Request request : requests) {
                request.setStatus(requestUpdateDTO.getStatus());
            }
        } else {
            for (Request request : requests) {
                if (confirmedRequestCount < event.getParticipantLimit()) {
                    request.setStatus(RequestStatus.CONFIRMED);
                    confirmedRequestCount++;
                } else {
                    request.setStatus(RequestStatus.REJECTED);
                }
            }
        }
        requestRepository.saveAll(requests);

        List<RequestOutDTO> confirmedRequests = requestMapper
                .toOutDTOList(requestRepository.findAllByEventAndStatus(eventId, RequestStatus.CONFIRMED));
        List<RequestOutDTO> rejectedRequests = requestMapper
                .toOutDTOList(requestRepository.findAllByEventAndStatus(eventId, RequestStatus.REJECTED));
        return RequestUpdateResultDTO.builder()
                .rejectedRequests(rejectedRequests)
                .confirmedRequests(confirmedRequests)
                .build();
    }
}
