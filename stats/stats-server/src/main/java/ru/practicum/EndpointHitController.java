package ru.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.StatsDto;
import ru.practicum.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class EndpointHitController {
    private final StatsService statsService;

    @PostMapping("/hit")
    EndpointHitDto addEndpointHit(@RequestBody EndpointHitDto endpointHitDto) {
        log.info("Запрос на добавление EndpointHit = {}", endpointHitDto);
        return statsService.addEndpointHit(endpointHitDto);
    }

    @GetMapping("/stats")
    List<StatsDto> getStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                            @RequestParam(required = false) List<String> uris,
                            @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("Запрос на получение статистики start = {}, end = {}, uris = {}, unique = {}", start, end, uris, unique);

        return statsService.getStats(start, end, uris, unique);
    }

}
