package ru.practicum.service.event;

import org.springframework.web.client.RestTemplate;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.StatsDto;
import ru.practicum.model.event.Event;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class EventUtil {
    private static final String DATE_TIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static List<StatsDto> getStatsDtoListForEventList(List<Event> eventList, Boolean unique) {
        StatsClient statsClient = new StatsClient(new RestTemplate());
        List<String> uris = eventList.stream().map(event -> "/events/" + event.getId()).toList();
        LocalDateTime startLdt = eventList.stream().map(Event::getCreatedOn).sorted().findFirst().get();
        String start = startLdt.format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN));
        String end = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN));
        return statsClient.getStats(start, end, uris, unique.toString());
    }

    public static Integer getEndpointHits(Event event, Boolean unique) {
        StatsClient statsClient = new StatsClient(new RestTemplate());
        String start = event.getCreatedOn().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN));
        String end = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN));
        List<String> uris = List.of("/events/" + event.getId());
        List<StatsDto> statsDtoList = statsClient.getStats(start, end, uris, unique.toString());
        if (statsDtoList.isEmpty()) {
            return 0;
        }
        return statsDtoList.getFirst().getHits();
    }
}
