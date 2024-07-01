package ru.practicum.explorewithme.hit;

import ru.practicum.explorewithme.dto.HitDTO;
import ru.practicum.explorewithme.dto.StatsDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface HitService {
    void saveHit(HitDTO hitDTO);

    List<StatsDTO> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}