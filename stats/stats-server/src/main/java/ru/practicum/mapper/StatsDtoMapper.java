package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.dto.StatsDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface StatsDtoMapper {

    StatsDto toStatsDto(String app, String uri, Integer hits);
}