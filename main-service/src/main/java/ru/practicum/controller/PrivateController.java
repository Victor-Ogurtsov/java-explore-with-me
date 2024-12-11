package ru.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.comment.dto.CommentDto;
import ru.practicum.model.comment.dto.NewComment;
import ru.practicum.model.event.dto.EventFullDto;
import ru.practicum.model.event.dto.EventShortDto;
import ru.practicum.model.event.dto.NewEventDto;
import ru.practicum.model.event.update.UpdateEventUserRequest;
import ru.practicum.model.participant.dto.ParticipationRequestDto;
import ru.practicum.model.status.EventRequestStatusUpdateRequest;

import ru.practicum.model.status.EventRequestStatusUpdateResult;
import ru.practicum.service.comment.CommentService;
import ru.practicum.service.event.EventService;
import ru.practicum.service.participant.ParticipationRequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@Slf4j
@RequiredArgsConstructor
public class PrivateController {

    private final EventService eventService;
    private final ParticipationRequestService participationRequestService;
    private final CommentService commentService;

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    EventFullDto addEvent(@PathVariable Long userId, @RequestBody @Valid NewEventDto newEventDto) {
        log.info("Запрос на добавление события от userId = {}, newEventDto = {}", userId, newEventDto);
        return eventService.addEvent(userId, newEventDto);
    }

    @GetMapping("/{userId}/events")
    List<EventShortDto> getEventList(@PathVariable Long userId,
                                     @RequestParam(defaultValue = "0") Integer from,
                                     @RequestParam(defaultValue = "10") Integer size) {
        log.info("Запрос на получение списка событий, добавленных текущим пользователем userId = {}, from = {}, size = {}",
                userId, from, size);
        return eventService.getEventList(userId, from, size);
    }

    @GetMapping("{userId}/events/{eventId}")
    EventFullDto getEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Запрос на получение события добавленного текущим пользователем userId = {}, eventId = {}",
                userId, eventId);
        return eventService.getEvent(userId, eventId);
    }

    @PatchMapping("{userId}/events/{eventId}")
    EventFullDto updateEvent(@PathVariable Long userId, @PathVariable Long eventId, @RequestBody @Valid UpdateEventUserRequest updateEventUserRequest) {
        log.info("Запрос на обновление события добавленного текущим пользователем userId = {}, eventId = {}, newEventDto = {}",
                userId, eventId, updateEventUserRequest);
        return eventService.updateEvent(userId, eventId, updateEventUserRequest);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    List<ParticipationRequestDto> getParticipationRequestByEventId(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Запрос на получение списка запросов на участие в событии пользователя userId = {}, eventId = {}", userId, eventId);
        return participationRequestService.getParticipationRequestByEventId(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    EventRequestStatusUpdateResult updateStatusParticipationRequest(@PathVariable Long userId,
                                                                    @PathVariable Long eventId,
                                                                    @RequestBody EventRequestStatusUpdateRequest request) {
        log.info("Запрос на изменение статуса списка запросов на участие в событии пользователя userId = {}, eventId = {}, " +
                "eventRequestStatusUpdateRequestDto = {}", userId, eventId, request);
        return participationRequestService.updateStatusParticipationRequest(userId, eventId, request);
    }

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    ParticipationRequestDto addParticipationRequest(@PathVariable Long userId, @RequestParam Long eventId) {
        log.info("Запрос на участие в событии userId = {}, eventId = {}", userId, eventId);
        return participationRequestService.addParticipationRequest(userId, eventId);
    }

    @GetMapping("/{userId}/requests")
    List<ParticipationRequestDto> getParticipationRequestListForRequester(@PathVariable Long userId) {
        log.info("Запрос на получение списка своих запросов на участие в событиях от пользователя userId = {}", userId);
        return participationRequestService.getParticipationRequestDtoListForRequester(userId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    ParticipationRequestDto cancelParticipationRequest(@PathVariable Long userId, @PathVariable Long requestId) {
        log.info("Запрос на отмену запроса на участие в событии от пользователя userId = {}, requestId = {}", userId, requestId);
        return participationRequestService.revokeParticipationRequest(userId, requestId);
    }

    @PostMapping("/{userId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    CommentDto addComments(@PathVariable Long userId, @RequestParam Long eventId, @RequestBody @Valid NewComment newComment) {
        log.info("Запрос на добавление комментария в событии userId = {}, eventId = {}, newComment = {} ", userId,
                eventId, newComment);
        return commentService.addComments(userId, eventId, newComment);
    }

    @PatchMapping("/{userId}/comments/{commentId}")
    CommentDto updateComments(@PathVariable Long userId, @PathVariable Long commentId, @RequestBody @Valid NewComment newComment) {
        log.info("Запрос на обновления комментария в событии userId = {}, commentId = {}, newComment = {} ", userId,
                commentId, newComment);
        return commentService.updateComments(userId, commentId, newComment);
    }
}
