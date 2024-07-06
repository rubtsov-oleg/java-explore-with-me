package ru.practicum.explorewithme.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.client.StatsClient;
import ru.practicum.explorewithme.event.EventMapper;
import ru.practicum.explorewithme.event.EventRepository;
import ru.practicum.explorewithme.request.RequestRepository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;
    private final EventMapper eventMapper;
    private final EventRepository eventRepository;
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
    public CompilationOutDTO saveCompilation(CompilationDTO compilationDTO) {
        Compilation compilation = compilationMapper.toModel(compilationDTO);
        return compilationMapper.toOutDTO(
                compilationRepository.save(compilation),
                eventRepository, eventMapper,
                statsClient, requestRepository
        );
    }

    @Override
    @Transactional
    public void deleteCompilation(Integer comId) {
        compilationRepository.deleteById(comId);
    }

    @Override
    @Transactional
    public CompilationOutDTO updateCompilation(CompilationDTO compilationDTO, Integer comId) {
        Compilation compilation = compilationRepository.findById(comId)
                .orElseThrow(() -> new NoSuchElementException("Событие " + comId + " не найдено"));

        if (compilationDTO.getEvents() != null) {
            compilation.setEvents(compilationDTO.getEvents());
        }
        if (compilationDTO.getPinned() != null) {
            compilation.setPinned(compilationDTO.getPinned());
        }
        if (compilationDTO.getTitle() != null && !compilationDTO.getTitle().isBlank()) {
            compilation.setTitle(compilationDTO.getTitle());
        }

        Compilation savedCompilation = compilationRepository.save(compilation);
        return compilationMapper.toOutDTO(
                savedCompilation, eventRepository, eventMapper, statsClient, requestRepository
        );
    }

    @Override
    public CompilationOutDTO getCompilationById(Integer comId) {
        return compilationMapper.toOutDTO(
                compilationRepository.findById(comId)
                        .orElseThrow(() -> new NoSuchElementException("Событие " + comId + " не найдено")),
                eventRepository, eventMapper, statsClient, requestRepository
        );
    }

    @Override
    public List<CompilationOutDTO> getAllCompilations(Integer from, Integer size, Boolean pinned) {
        List<Compilation> compilations;
        if (pinned != null) {
            compilations = compilationRepository.findAllByPinned(
                    pinned, PageRequest.of(from / size, size)
            ).getContent();
        } else {
            compilations = compilationRepository.findAll(PageRequest.of(from / size, size)).getContent();
        }
        return compilationMapper.toOutDTOList(
                compilations, eventRepository, eventMapper, statsClient, requestRepository
        );
    }
}
