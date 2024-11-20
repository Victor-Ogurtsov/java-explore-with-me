package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.EndpointHit;
import ru.practicum.dto.EndpointHitDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EndpointHitMapper {

    @Mapping(target = "timestamp", source = "endpointHitDto.timestamp",
            dateFormat = "yyyy-MM-dd HH:mm:ss")
    EndpointHit fromEndpointHitDto(EndpointHitDto endpointHitDto);

    @Mapping(target = "timestamp", source = "endpointHit.timestamp",
            dateFormat = "yyyy-MM-dd HH:mm:ss")
    EndpointHitDto toEndpointHitDto(EndpointHit endpointHit);
}