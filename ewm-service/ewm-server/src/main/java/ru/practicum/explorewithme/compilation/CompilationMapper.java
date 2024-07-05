package ru.practicum.explorewithme.compilation;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.practicum.explorewithme.client.StatsClient;
import ru.practicum.explorewithme.event.EventMapper;
import ru.practicum.explorewithme.event.EventRepository;
import ru.practicum.explorewithme.event.dto.EventShortDTO;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.request.RequestRepository;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Mapper(componentModel = "spring", uses = {EventMapper.class})
public interface CompilationMapper {
    @Mapping(target = "pinned", defaultValue = "false")
    Compilation toModel(CompilationDTO compilationDTO);

    @Mapping(target = "events", source = "events", qualifiedByName = "mapEventIdsToEventShortDTOs")
    CompilationOutDTO toOutDTO(Compilation compilation,
                               @Context EventRepository eventRepository,
                               @Context EventMapper eventMapper,
                               @Context StatsClient statsClient,
                               @Context RequestRepository requestRepository);

    List<CompilationOutDTO> toOutDTOList(List<Compilation> compilations,
                                         @Context EventRepository eventRepository,
                                         @Context EventMapper eventMapper,
                                         @Context StatsClient statsClient,
                                         @Context RequestRepository requestRepository);

    @Named("mapEventIdsToEventShortDTOs")
    default List<EventShortDTO> mapEventIdsToEventShortDTOs(List<Integer> eventIds,
                                                            @Context EventRepository eventRepository,
                                                            @Context EventMapper eventMapper,
                                                            @Context StatsClient statsClient,
                                                            @Context RequestRepository requestRepository) {
        if (eventIds == null || eventIds.isEmpty()) {
            return Collections.emptyList();
        }
        List<Event> events = eventRepository.findAllById(eventIds);
        if (events.size() != eventIds.size()) {
            throw new NoSuchElementException("One or more events not found");
        }
        return eventMapper.toShortDTOList(events, statsClient, requestRepository);
    }
}
