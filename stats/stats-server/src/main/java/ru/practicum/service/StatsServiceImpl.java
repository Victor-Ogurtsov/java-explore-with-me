package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.EndpointHit;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.mapper.EndpointHitMapper;
import ru.practicum.EndpointHitRepository;
import ru.practicum.dto.StatsDto;
import ru.practicum.mapper.StatsDtoMapper;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final EndpointHitMapper endpointHitMapper;
    private final StatsDtoMapper statsDtoMapper;
    private final EndpointHitRepository endpointHitRepository;

    @Override
    public EndpointHitDto addEndpointHit(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = endpointHitMapper.fromEndpointHitDto(endpointHitDto);
        System.out.println("endpointHit = " + endpointHit);
        EndpointHit addedEndpointHit = endpointHitRepository.save(endpointHit);
        return endpointHitMapper.toEndpointHitDto(addedEndpointHit);
    }

    @Override
    public List<StatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {

        List<EndpointHit> endpointHitLis = endpointHitRepository.getEndpointHitListByStartAndEnd(start, end);

        if (!(uris == null)) {
            endpointHitLis = endpointHitLis.stream()
                    .filter(endpointHit -> uris.contains(endpointHit.getUri())).toList();
        }

        List<StatsDto> statsDtoList = new ArrayList<>();

        Map<String, List<EndpointHit>> endpointHitByAppMap = endpointHitLis.stream().collect(Collectors.groupingBy(EndpointHit::getApp));
        for (String app : endpointHitByAppMap.keySet()) {
            List<EndpointHit> endpointHitByAppList = endpointHitByAppMap.get(app);
            Map<String, List<EndpointHit>> endpointHitByUriMap = endpointHitByAppList.stream().collect(Collectors.groupingBy(EndpointHit::getUri));
            for (String uri : endpointHitByUriMap.keySet()) {
                List<EndpointHit> endpointHitByUriList = endpointHitByUriMap.get(uri);
                StatsDto statsDto;
                if (unique) {
                    Map<String, List<EndpointHit>> endpointHitByIpMap = endpointHitByUriList.stream().collect(Collectors.groupingBy(EndpointHit::getIp));
                    statsDto = statsDtoMapper.toStatsDto(app, uri, endpointHitByIpMap.keySet().size());
                } else {
                    statsDto = statsDtoMapper.toStatsDto(app, uri, endpointHitByUriList.size());
                }
                statsDtoList.add(statsDto);
            }
        }
        return statsDtoList.stream().sorted(Comparator.comparing(StatsDto::getHits).reversed()).toList();
    }
}
