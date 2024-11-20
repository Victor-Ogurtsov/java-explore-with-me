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
import java.util.Map;

@Component
@RequiredArgsConstructor
public class StatsClient {
    private final RestTemplate restTemplate;

    EndpointHitDto addEndpointHit(EndpointHitDto endpointHitDto) {
        HttpEntity<EndpointHitDto> request = new HttpEntity<>(endpointHitDto);
        ResponseEntity<EndpointHitDto> response = restTemplate
                .exchange("http://localhost:9090/hit", HttpMethod.POST, request, EndpointHitDto.class);
        return response.getBody();
    }

    List<StatsDto> getStats(String start, String end, List<String> uris, String unique) {
        Map<String, Object> parameters = new java.util.HashMap<>(Map.of(
                "start", start,
                "end", end,
                "unique", unique));
        for (String uri : uris) {
            parameters.put("uris", uri);
        }

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:9090/stats")
                .queryParam("start", start)
                .queryParam("end", end)
                .queryParam("unique", unique);
        for (String uri : uris) {
            builder.queryParam("uris", uri);
        }
        System.out.println(builder.build().toUri());

        ResponseEntity<List<StatsDto>> response = restTemplate
                .exchange(builder.build().toUri(), HttpMethod.GET, new HttpEntity<>() {}, new ParameterizedTypeReference<List<StatsDto>>() {});
        return response.getBody();
    }
}