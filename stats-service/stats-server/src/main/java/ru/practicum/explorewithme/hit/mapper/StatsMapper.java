package ru.practicum.explorewithme.hit.mapper;

import org.mapstruct.Mapper;
import ru.practicum.explorewithme.dto.StatsDTO;
import ru.practicum.explorewithme.hit.model.Stats;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StatsMapper {
    List<StatsDTO> toListDTO(List<Stats> statsList);
}
