package ru.practicum.service.event;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.model.event.Event;
import ru.practicum.model.params.ParamsFilterForEvents;
import ru.practicum.model.event.dto.*;
import ru.practicum.model.event.update.UpdateEventAdminRequest;
import ru.practicum.model.event.update.UpdateEventUserRequest;
import ru.practicum.model.params.ParamsFilterForPublicEvents;

import java.util.List;


public interface EventService {

    EventFullDto addEvent(Long userId, NewEventDto newEventDto);

    List<EventShortDto> getEventList(Long userId, Integer from, Integer size);

    EventFullDto getEvent(Long userId, Long eventId);

    EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    List<EventFullDto> getEventFullListForAdmin(ParamsFilterForEvents paramsFilterForEvents);

    EventFullDto updateEventFromAdmin(Long eventId, UpdateEventAdminRequest updateEventAdminRequest);

    List<EventFullDto> getPublicEventsByParams(ParamsFilterForPublicEvents params, HttpServletRequest request);

    EventFullDto getPublicEvent(Long id,  HttpServletRequest request);

    List<EventShortDto> getEventShortDtoListFromEventList(List<Event> eventList);

    List<EventFullDto> getEventFulltDtoListFromEventList(List<Event> eventList);
}
