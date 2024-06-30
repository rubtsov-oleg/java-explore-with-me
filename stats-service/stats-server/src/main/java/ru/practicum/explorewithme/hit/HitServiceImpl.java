package ru.practicum.explorewithme.hit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.HitDTO;
import ru.practicum.explorewithme.dto.StatsDTO;
import ru.practicum.explorewithme.hit.mapper.HitMapper;
import ru.practicum.explorewithme.hit.mapper.StatsMapper;
import ru.practicum.explorewithme.hit.model.Stats;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HitServiceImpl implements HitService {
    private final HitMapper hitMapper;
    private final StatsMapper statsMapper;
    private final HitRepository hitRepository;

    @Override
    @Transactional
    public void saveHit(HitDTO hitDTO) {
        hitRepository.save(hitMapper.toModel(hitDTO));
    }

    @Override
    public List<StatsDTO> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        List<Stats> statsList;
        if (uris == null || uris.isEmpty()) {
            if (unique) {
                statsList = hitRepository.findUniqueHitsStatisticsWithoutUris(start, end);
            } else {
                statsList = hitRepository.findHitsStatisticsWithoutUris(start, end);
            }
        } else {
            if (unique) {
                statsList = hitRepository.findUniqueHitsStatisticsWithUris(start, end, uris);
            } else {
                statsList = hitRepository.findHitsStatisticsWithUris(start, end, uris);
            }
        }
        return statsMapper.toListDTO(statsList);
    }
}