package ru.practicum.client;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.StatsDto;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StatsClient {
    private final RestTemplate restTemplate;

    public EndpointHitDto addEndpointHit(EndpointHitDto endpointHitDto) {
        HttpEntity<EndpointHitDto> request = new HttpEntity<>(endpointHitDto);
        ResponseEntity<EndpointHitDto> response = restTemplate
                .exchange("http://localhost:9090/hit", HttpMethod.POST, request, EndpointHitDto.class);
        return response.getBody();
    }

    public List<StatsDto> getStats(String start, String end, List<String> uris, String unique) {

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:9090/stats")
                .queryParam("start", start)
                .queryParam("end", end)
                .queryParam("unique", unique);
        for (String uri : uris) {
            builder.queryParam("uris", uri);
        }

        ResponseEntity<List<StatsDto>> response = restTemplate
                .exchange(builder.build().encode().toUri(), HttpMethod.GET, new HttpEntity<>() {}, new ParameterizedTypeReference<List<StatsDto>>() {});
        return response.getBody();
    }
}