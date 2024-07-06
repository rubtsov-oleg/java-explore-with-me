package ru.practicum.explorewithme.event;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.practicum.explorewithme.category.*;
import ru.practicum.explorewithme.client.StatsClient;
import ru.practicum.explorewithme.comment.CommentMapper;
import ru.practicum.explorewithme.comment.CommentRepository;
import ru.practicum.explorewithme.comment.dto.CommentShortDTO;
import ru.practicum.explorewithme.dto.StatsDTO;
import ru.practicum.explorewithme.event.dto.EventDTO;
import ru.practicum.explorewithme.event.dto.EventOutDTO;
import ru.practicum.explorewithme.event.dto.EventShortDTO;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.request.RequestRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;

@Mapper(componentModel = "spring")
public interface EventMapper {
    @Mapping(target = "location.lat", source = "location.lat")
    @Mapping(target = "location.lon", source = "location.lon")
    @Mapping(target = "paid", defaultValue = "false")
    @Mapping(target = "requestModeration", defaultValue = "true")
    @Mapping(target = "participantLimit", defaultValue = "0")
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryFromId")
    Event toModel(EventDTO eventDTO, @Context CategoryRepository categoryRepository);

    @Mapping(target = "location.lat", source = "location.lat")
    @Mapping(target = "location.lon", source = "location.lon")
    @Mapping(target = "views", source = "id", qualifiedByName = "mapViews")
    @Mapping(target = "confirmedRequests", source = "id", qualifiedByName = "mapCountConfirmedRequests")
    @Mapping(target = "comments", source = "id", qualifiedByName = "mapComments")
    EventOutDTO toOutDTO(Event event, @Context StatsClient statsClient,
                         @Context RequestRepository requestRepository,
                         @Context CommentRepository commentRepository,
                         @Context CommentMapper commentMapper);

    @Mapping(target = "views", source = "id", qualifiedByName = "mapViews")
    @Mapping(target = "confirmedRequests", source = "id", qualifiedByName = "mapCountConfirmedRequests")
    EventShortDTO toShortDTO(
            Event event, @Context StatsClient statsClient, @Context RequestRepository requestRepository
    );

    List<EventShortDTO> toShortDTOList(
            List<Event> events, @Context StatsClient statsClient, @Context RequestRepository requestRepository
    );

    List<EventOutDTO> toOutDTOList(
            List<Event> events, @Context StatsClient statsClient, @Context RequestRepository requestRepository,
            @Context CommentRepository commentRepository,
            @Context CommentMapper commentMapper
    );

    @Named("categoryFromId")
    default Category categoryFromId(Integer id, @Context CategoryRepository categoryRepository) {
        if (id == null) {
            return null;
        }
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Категория с ID " + id + " не найдена"));
    }

    @Named("mapViews")
    default Long mapViews(Integer eventId, @Context StatsClient statsClient) {
        String start = LocalDateTime.now().minusYears(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String end = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        List<StatsDTO> stats = statsClient.getStats(start, end, List.of("/events/" + eventId), true);
        return stats.stream()
                .mapToLong(StatsDTO::getHits)
                .sum();
    }

    @Named("mapCountConfirmedRequests")
    default Long mapCountConfirmedRequests(Integer eventId, @Context RequestRepository requestRepository) {
        return requestRepository.countByEventIdAndStatusConfirmed(eventId);
    }

    @Named("mapComments")
    default List<CommentShortDTO> mapComments(Integer eventId, @Context CommentRepository commentRepository,
                                              @Context CommentMapper commentMapper) {
        return commentMapper.toShortDTOList(commentRepository.findAllByEventIdOrderByCreatedDesc(eventId));
    }
}
