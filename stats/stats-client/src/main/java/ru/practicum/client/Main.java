package ru.practicum.client;

import org.springframework.web.client.RestTemplate;

import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.StatsDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        StatsClient statsClient = new StatsClient(new RestTemplate());

        List<StatsDto> statsDtoList = statsClient.getStats(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                LocalDateTime.now().plusYears(3).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                List.of("/events/1", "/events/2"), "true");
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println(statsDtoList);

        EndpointHitDto endpointHitDto = new EndpointHitDto();
        endpointHitDto.setApp("ewm-main-service");
        endpointHitDto.setUri("/events/1");
        endpointHitDto.setIp("192.163.0.1");
        endpointHitDto.setTimestamp("2022-09-06 11:00:23");

        EndpointHitDto endpointHitDtoRes = statsClient.addEndpointHit(endpointHitDto);
        System.out.println(endpointHitDtoRes);
    }
}