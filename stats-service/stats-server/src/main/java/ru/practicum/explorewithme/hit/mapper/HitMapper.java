package ru.practicum.explorewithme.hit.mapper;

import org.mapstruct.Mapper;
import ru.practicum.explorewithme.dto.HitDTO;
import ru.practicum.explorewithme.hit.model.Hit;

@Mapper(componentModel = "spring")
public interface HitMapper {
    Hit toModel(HitDTO hitDTO);

    HitDTO toDTO(Hit hit);
}