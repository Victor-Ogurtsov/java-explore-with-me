package ru.practicum.service.event;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.StatsDto;
import ru.practicum.exception.IncorrectRequestException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ViolationRestrictException;
import ru.practicum.model.event.update.UpdateEventAdminRequest;
import ru.practicum.model.event.update.UpdateEventBase;
import ru.practicum.model.event.update.UpdateEventUserRequest;
import ru.practicum.model.params.ParamsFilterForPublicEvents;
import ru.practicum.model.participant.ParticipationRequest;
import ru.practicum.model.sort.Sort;
import ru.practicum.model.state.State;
import ru.practicum.model.category.Category;
import ru.practicum.model.event.Event;
import ru.practicum.model.params.ParamsFilterForEvents;
import ru.practicum.model.event.QEvent;
import ru.practicum.model.event.dto.*;
import ru.practicum.model.location.Location;
import ru.practicum.model.state.StateActionAdmin;
import ru.practicum.model.state.StateActionUser;
import ru.practicum.model.user.User;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.ParticipationRequestRepository;
import ru.practicum.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final NewEventMapper newEventMapper;
    private final EventFullMapper eventFullMapper;
    private final EventShortMapper eventShortMapper;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final ParticipationRequestRepository participationRequestRepository;
    private final StatsClient statsClient;
    private final JPAQueryFactory jpaQueryFactory;
    private static final String DATE_TIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String APP_NAME = "explore-with-me";

    @Override
    public EventFullDto addEvent(Long userId, NewEventDto newEventDto) {
        User user = getUserOrThrowException(userId);
        Category category = getCategoryOrThrowException(newEventDto.getCategory());
        Event event = newEventMapper.fromNewEventDto(newEventDto);

        if (event.getEventDate().isBefore(LocalDateTime.now())) {
            throw new IncorrectRequestException("BAD_REQUEST", "Incorrectly made request.",
                    "Field: eventDate. Error: должно содержать дату, которая еще не наступила. Value: " + event.getEventDate());
        }

        event.setInitiator(user);
        event.setCategory(category);
        Event addedEvent = eventRepository.save(event);
        return getEventFullDtoFromEvent(addedEvent);
    }

    @Override
    public List<EventShortDto> getEventList(Long userId, Integer from, Integer size) {
        getUserOrThrowException(userId);
        List<EventShortDto> eventShortDtoList = new ArrayList<>();
        List<Event> eventList = jpaQueryFactory.selectFrom(QEvent.event)
                .where(QEvent.event.initiator.id.eq(userId))
                .offset(from).limit(size).fetch();

        if (!eventList.isEmpty()) {
            eventShortDtoList = getEventShortDtoListFromEventList(eventList);
        }
        return eventShortDtoList;
    }

    @Override
    public List<EventShortDto> getEventShortDtoListFromEventList(List<Event> eventList) {
        List<StatsDto> statsDtoList = EventUtil.getStatsDtoListForEventList(eventList, false);
        Map<String, List<StatsDto>> statsDtoByUriMap = statsDtoList.stream().collect(Collectors.groupingBy(StatsDto::getUri));
        List<ParticipationRequest> participationRequestList = participationRequestRepository
                .findAlLConfirmedParticipationRequestListByEventIdList(eventList.stream().map(Event::getId).toList());
        List<EventShortDto> eventShortDtoList = new ArrayList<>();

        for (Event event : eventList) {
            EventShortDto eventShortDto;
            Integer confirmedRequests = participationRequestList.stream()
                    .filter(participationRequest -> participationRequest.getEvent().getId().equals(event.getId())).toList().size();
            String key = "/events/" + event.getId();
            Integer views = 0;
            if (statsDtoByUriMap.containsKey(key)) {
                views = statsDtoByUriMap.get(key).getFirst().getHits();
            }
            eventShortDto = eventShortMapper.toEventShort(event, views, confirmedRequests);
            eventShortDtoList.add(eventShortDto);
        }
        return eventShortDtoList;
    }

    @Override
    public List<EventFullDto> getEventFulltDtoListFromEventList(List<Event> eventList) {
        List<StatsDto> statsDtoList = EventUtil.getStatsDtoListForEventList(eventList, false);
        Map<String, List<StatsDto>> statsDtoByUriMap = statsDtoList.stream().collect(Collectors.groupingBy(StatsDto::getUri));
        List<ParticipationRequest> participationRequestList = participationRequestRepository
                .findAlLConfirmedParticipationRequestListByEventIdList(eventList.stream().map(Event::getId).toList());
        List<EventFullDto> eventFullDtoList = new ArrayList<>();

        for (Event event : eventList) {
            EventFullDto eventFullDto;
            Integer confirmedRequests = participationRequestList.stream()
                    .filter(participationRequest -> participationRequest.getEvent().getId().equals(event.getId())).toList().size();
            String key = "/events/" + event.getId();
            Integer views = 0;
            if (statsDtoByUriMap.containsKey(key)) {
                views = statsDtoByUriMap.get(key).getFirst().getHits();
            }
            eventFullDto = eventFullMapper.toEventFullDto(event, views, confirmedRequests);
            eventFullDtoList.add(eventFullDto);
        }
        return eventFullDtoList;
    }

    @Override
    public EventFullDto getEvent(Long userId, Long eventId) {
        getUserOrThrowException(userId);
        Event event = getEventOrThrowException(eventId);
        return getEventFullDtoFromEvent(event);
    }

    @Override
    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        getUserOrThrowException(userId);
        Event event = getEventOrThrowException(eventId);
        if (event.getState() == State.PUBLISHED) {
            throw new ViolationRestrictException("FORBIDDEN",
                    "For the requested operation the conditions are not met.",
                    "Only pending or canceled events can be changed");
        }

        Event newEvent = updatingFieldsEvent(event, updateEventUserRequest);
        Event updatedEvent = eventRepository.save(newEvent);
        return getEventFullDtoFromEvent(updatedEvent);
    }

    private EventFullDto getEventFullDtoFromEvent(Event event) {
        Integer views = EventUtil.getEndpointHits(event, true);
        List<ParticipationRequest> participationRequestList = participationRequestRepository
                .findAlLConfirmedParticipationRequestListByEventIdList(List.of(event.getId()));
        Integer confirmedRequests = participationRequestList.size();
        return eventFullMapper.toEventFullDto(event, views, confirmedRequests);
    }

    @Override
    public List<EventFullDto> getEventFullListForAdmin(ParamsFilterForEvents paramsFilterForEvents) {
        List<EventFullDto> eventFullDtoList = new ArrayList<>();

        JPAQuery<Event> jpaQuery = getJPAQueryForEventListForAdmin(paramsFilterForEvents);
        List<Event> eventList = jpaQuery.fetch();
        if (!eventList.isEmpty()) {
            eventFullDtoList = getEventFulltDtoListFromEventList(eventList);
        }
        return eventFullDtoList;
    }

    @Override
    public EventFullDto updateEventFromAdmin(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event event = getEventOrThrowException(eventId);
        if (event.getState() == State.PUBLISHED && updateEventAdminRequest.getStateAction() == StateActionAdmin.PUBLISH_EVENT) {
            throw new ViolationRestrictException("FORBIDDEN",
                    "For the requested operation the conditions are not met.",
                    "Cannot publish the event because it's not in the right state: PUBLISHED");
        }
        if (event.getState() == State.PUBLISHED && updateEventAdminRequest.getStateAction() == StateActionAdmin.REJECT_EVENT) {
            throw new ViolationRestrictException("FORBIDDEN",
                    "For the requested operation the conditions are not met.",
                    "Cобытие можно отклонить, только если оно еще не опубликовано");
        }
        if (event.getState() == State.CANCELED && updateEventAdminRequest.getStateAction() == StateActionAdmin.PUBLISH_EVENT) {
            throw new ViolationRestrictException("FORBIDDEN",
                    "For the requested operation the conditions are not met.",
                    "Нельзя опубликовать отмененное событие");
        }

        Event newEvent = updatingFieldsEvent(event, updateEventAdminRequest);
        Event updatedEvent = eventRepository.save(newEvent);
        return getEventFullDtoFromEvent(updatedEvent);
    }

    @Override
    public List<EventFullDto> getPublicEventsByParams(ParamsFilterForPublicEvents params, HttpServletRequest request) {
        checkParamsFilterForPublicEvents(params);
        List<EventFullDto> eventFullDtoList = new ArrayList<>();

        JPAQuery<Event> jpaQuery = getJPAQueryForEventListForPublic(params);
        List<Event> eventList = jpaQuery.fetch();

        if (!eventList.isEmpty()) {
            eventFullDtoList = getEventFulltDtoListFromEventList(eventList);

            if (params.getSort() == Sort.EVENT_DATE) {
                eventFullDtoList = eventFullDtoList.stream().sorted(Comparator.comparing(EventFullDto::getEventDate)).toList();
            } else if (params.getSort() == Sort.VIEWS) {
                eventFullDtoList = eventFullDtoList.stream().sorted(Comparator.comparing(EventFullDto::getViews)).toList();
            }

            if (params.getOnlyAvailable() != null && params.getOnlyAvailable()) {
                eventFullDtoList = eventFullDtoList.stream()
                        .filter(eventFullDto -> eventFullDto.getConfirmedRequests() < eventFullDto.getParticipantLimit()
                                || eventFullDto.getParticipantLimit() == 0)
                        .toList();
            }
        }

        statsClient.addEndpointHit(getEndpointHitDto(request));
        return eventFullDtoList;
    }

    @Override
    public EventFullDto getPublicEvent(Long eventId, HttpServletRequest request) {
        Event event = eventRepository.getPublishedEventById(eventId)
                .orElseThrow(() -> new NotFoundException("NOT_FOUND", "The required object was not found.",
                        "Event with id=" + eventId + " was not found"));
        statsClient.addEndpointHit(getEndpointHitDto(request));
        return getEventFullDtoFromEvent(event);
    }

    private EndpointHitDto getEndpointHitDto(HttpServletRequest request) {
        EndpointHitDto endpointHitDto = new EndpointHitDto();
        endpointHitDto.setApp(APP_NAME);
        endpointHitDto.setUri(request.getRequestURI());
        endpointHitDto.setIp(request.getRemoteAddr());
        endpointHitDto.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN)));
        return endpointHitDto;
    }

    private User getUserOrThrowException(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("NOT_FOUND", "The required object was not found.",
                        "User with id=" + userId + " was not found"));
    }

    private Category getCategoryOrThrowException(Long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("NOT_FOUND", "The required object was not found.",
                        "Category with id=" + catId + " was not found"));
    }

    private Event getEventOrThrowException(Long eventId) {
        return eventRepository.getEventById(eventId)
                .orElseThrow(() -> new NotFoundException("NOT_FOUND", "The required object was not found.",
                        "Category with id=" + eventId + " was not found"));
    }

    private Event updatingFieldsEvent(Event event, UpdateEventBase updateEventBase) {
        String annotation = updateEventBase.getAnnotation();
        if (annotation != null) {
            if (annotation.isBlank()) {
                throw new IncorrectRequestException("BAD_REQUEST", "Incorrectly made request.",
                        "Аннотация не должна быть пустой!");
            }
            event.setAnnotation(annotation);
        }

        Long catId = updateEventBase.getCategory();
        if (catId != null) {
            Category newCategory = getCategoryOrThrowException(catId);
            event.setCategory(newCategory);
        }

        String description = updateEventBase.getDescription();
        if (description != null) {
            if (description.isBlank()) {
                throw new IncorrectRequestException("BAD_REQUEST", "Incorrectly made request.",
                        "описание  не должна быть пустым!");
            }
            event.setDescription(description);
        }

        String eventDate = updateEventBase.getEventDate();
        if (eventDate != null) {
            LocalDateTime eventDateLdt = LocalDateTime.parse(eventDate,
                    DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN));
            if (eventDateLdt.isBefore(LocalDateTime.now())) {
                throw new IncorrectRequestException("BAD_REQUEST", "Incorrectly made request.",
                        "Field: eventDate. Error: должно содержать дату, которая еще не наступила. Value: " + event.getEventDate());
            }
            event.setEventDate(eventDateLdt);
        }

        Location location = updateEventBase.getLocation();
        if (location != null) {
            event.setLocationLat(location.getLat());
            event.setLocationLon(location.getLon());
        }

        Boolean paid = updateEventBase.getPaid();
        if (paid != null) {
            event.setPaid(paid);
        }

        Integer participantLimit = updateEventBase.getParticipantLimit();
        if (participantLimit != null) {
            event.setParticipantLimit(participantLimit);
        }

        Boolean requestModeration = updateEventBase.getRequestModeration();
        if (requestModeration != null) {
            event.setRequestModeration(requestModeration);
        }

        String title = updateEventBase.getTitle();
        if (title != null) {
            if (title.isBlank()) {
                throw new IncorrectRequestException("BAD_REQUEST", "Incorrectly made request.",
                        "Заголовок не должен быть пустой!");
            }
            event.setTitle(title);
        }

        if (updateEventBase instanceof UpdateEventUserRequest) {
            StateActionUser stateAction = ((UpdateEventUserRequest) updateEventBase).getStateAction();
            if (stateAction != null) {
                if (stateAction == StateActionUser.CANCEL_REVIEW) {
                    event.setState(State.CANCELED);
                } else if (stateAction == StateActionUser.SEND_TO_REVIEW) {
                    event.setState(State.PENDING);
                }
            }
        }

        if (updateEventBase instanceof UpdateEventAdminRequest) {
            StateActionAdmin stateAction = ((UpdateEventAdminRequest) updateEventBase).getStateAction();
            if (stateAction != null) {
                if (stateAction == StateActionAdmin.REJECT_EVENT) {
                    event.setState(State.CANCELED);
                } else if (stateAction == StateActionAdmin.PUBLISH_EVENT) {
                    event.setState(State.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                }
            }
        }
        return event;
    }

    private JPAQuery<Event> getJPAQueryForEventListForAdmin(ParamsFilterForEvents paramsFilterForEvents) {
        JPAQuery<Event> jpaQuery = jpaQueryFactory.selectFrom(QEvent.event)
                .offset(paramsFilterForEvents.getFrom()).limit(paramsFilterForEvents.getSize());

        List<Long> users = paramsFilterForEvents.getUsers();
        if (users != null) {
            jpaQuery = jpaQuery.where(QEvent.event.initiator.id.in(users));
        }

        List<State> states = paramsFilterForEvents.getStates();
        if (states != null) {
            jpaQuery = jpaQuery.where(QEvent.event.state.in(states));
        }

        List<Long> categories = paramsFilterForEvents.getCategories();
        if (categories != null) {
            jpaQuery = jpaQuery.where(QEvent.event.category.id.in(categories));
        }

        if (paramsFilterForEvents.getRangeStart() != null) {
            LocalDateTime rangeStart = LocalDateTime
                    .parse(paramsFilterForEvents.getRangeStart(), DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN));
            jpaQuery = jpaQuery.where(QEvent.event.eventDate.goe(rangeStart));
        }

        if (paramsFilterForEvents.getRangeEnd() != null) {
            LocalDateTime rangeEnd = LocalDateTime
                    .parse(paramsFilterForEvents.getRangeEnd(), DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN));
            jpaQuery = jpaQuery.where(QEvent.event.eventDate.loe(rangeEnd));
        }

        return jpaQuery;
    }

    private JPAQuery<Event> getJPAQueryForEventListForPublic(ParamsFilterForPublicEvents params) {
        JPAQuery<Event> jpaQuery = jpaQueryFactory.selectFrom(QEvent.event).where(QEvent.event.state.eq(State.PUBLISHED))
                .offset(params.getFrom()).limit(params.getSize());

        String text = params.getText();
        if (text != null && !text.isBlank()) {
            jpaQuery = jpaQuery.where(QEvent.event.annotation.containsIgnoreCase(text).or(QEvent.event.description.containsIgnoreCase(text)));
        }

        List<Long> categories = params.getCategories();
        if (categories != null && !categories.isEmpty()) {
            jpaQuery = jpaQuery.where(QEvent.event.category.id.in(categories));
        }

        Boolean paid = params.getPaid();
        if (paid != null) {
            jpaQuery = jpaQuery.where(QEvent.event.paid.eq(paid));
        }

        String rangeStart = params.getRangeStart();
        LocalDateTime rangeStartLdt;
        if (rangeStart != null) {
            rangeStartLdt = LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN));
        } else {
            rangeStartLdt = LocalDateTime.now();
        }
        jpaQuery = jpaQuery.where(QEvent.event.eventDate.goe(rangeStartLdt));

        String rangeEnd = params.getRangeEnd();
        if (rangeEnd != null) {
            LocalDateTime rangeEndLdt = LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN));
            jpaQuery = jpaQuery.where(QEvent.event.eventDate.loe(rangeEndLdt));
        }
        return jpaQuery;
    }


    private void checkParamsFilterForPublicEvents(ParamsFilterForPublicEvents params) {
        if (params.getRangeStart() != null && params.getRangeEnd() != null) {
            LocalDateTime start = LocalDateTime
                    .parse(params.getRangeStart(), DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN));
            LocalDateTime end = LocalDateTime
                    .parse(params.getRangeEnd(), DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN));
            if (start.isAfter(end)) {
                throw new IncorrectRequestException("BAD_REQUEST", "Incorrectly made request.",
                        "Дата начала выборки событий должна быть раньше даты конца выборки");
            }
        }
    }
}
