package ru.practicum.explorewithme.hit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.HitDTO;
import ru.practicum.explorewithme.dto.StatsDTO;
import ru.practicum.explorewithme.hit.mapper.HitMapper;
import ru.practicum.explorewithme.hit.mapper.StatsMapper;

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
        if (unique) {
            return statsMapper.toListDTO(hitRepository.findUniqueHitsStatistics(start, end, uris));
        }
        return statsMapper.toListDTO(hitRepository.findHitsStatistics(start, end, uris));
    }
}