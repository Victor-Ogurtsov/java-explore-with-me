package ru.practicum.service;

import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.StatsDto;

import java.util.List;

public interface StatsService {

    EndpointHitDto addEndpointHit(EndpointHitDto endpointHitDto);

    List<StatsDto> getStats(String start, String end, List<String> uris, Boolean unique);
}
