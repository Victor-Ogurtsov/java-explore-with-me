package ru.practicum.service.participant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ViolationRestrictException;
import ru.practicum.model.event.Event;
import ru.practicum.model.participant.ParticipationRequest;
import ru.practicum.model.participant.dto.ParticipationRequestDto;
import ru.practicum.model.status.EventRequestStatusUpdateRequest;
import ru.practicum.model.status.Status;
import ru.practicum.model.participant.dto.ParticipationRequestMapper;
import ru.practicum.model.state.State;
import ru.practicum.model.status.EventRequestStatusUpdateResult;
import ru.practicum.model.user.User;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.ParticipationRequestRepository;
import ru.practicum.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParticipationRequestServiceImpl implements ParticipationRequestService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ParticipationRequestRepository participationRequestRepository;
    private final ParticipationRequestMapper participationRequestMapper;

    @Override
    public ParticipationRequestDto addParticipationRequest(Long userId, Long eventId) {
        User user = getUserOrThrowException(userId);
        Event event = getEventOrThrowException(eventId);
        if (userId.equals(event.getInitiator().getId())) {
            throw new ViolationRestrictException("CONFLICT", "Integrity constraint has been violated.",
                    "Инициатор события не может добавить запрос на участие в своём событии");
        }
        if (event.getState() != State.PUBLISHED) {
            throw new ViolationRestrictException("CONFLICT", "Integrity constraint has been violated.",
                    "Нельзя участвовать в неопубликованном событии");
        }
        List<ParticipationRequest> participationRequestList = participationRequestRepository.findAlLByEventId(eventId);
        Optional<ParticipationRequest> opr = participationRequestList.stream()
                .filter(participationRequest -> participationRequest.getRequester().getId().equals(userId)).findFirst();
        if (opr.isPresent()) {
            throw new ViolationRestrictException("CONFLICT", "Integrity constraint has been violated.",
              "Пользователь с  userId = " + userId + " уже отправлял заявку на участие в событии с eventId = " + eventId);
        }
        List<ParticipationRequest> participationRequestConfirmedList = participationRequestList.stream()
                .filter(participationRequest -> participationRequest.getStatus().equals(Status.CONFIRMED)).toList();
        if (participationRequestConfirmedList.size() >= event.getParticipantLimit() && event.getParticipantLimit() != 0) {
            throw new ViolationRestrictException("CONFLICT", "Integrity constraint has been violated.",
                    " Достигнут лимит запросов на участие");
        }
        ParticipationRequest participationRequest = new ParticipationRequest();
        if (event.getRequestModeration().equals(false) || event.getParticipantLimit() == 0) {
            participationRequest.setStatus(Status.CONFIRMED);
        }
        participationRequest.setRequester(user);
        participationRequest.setEvent(event);
        ParticipationRequest addedParticipationRequest = participationRequestRepository.save(participationRequest);
        return participationRequestMapper.toParticipationRequestDto(addedParticipationRequest);
    }

    @Override
    public List<ParticipationRequestDto> getParticipationRequestDtoListForRequester(Long userId) {
        getUserOrThrowException(userId);
        List<ParticipationRequest> participationRequestList = participationRequestRepository.findAlLByUserId(userId);

        return participationRequestMapper.toParticipationRequestDtoList(participationRequestList);
    }

    @Override
    public ParticipationRequestDto revokeParticipationRequest(Long userId, Long requestId) {
        getUserOrThrowException(userId);
        ParticipationRequest participationRequest = participationRequestRepository.getByRequestId(requestId)
                .orElseThrow(() -> new NotFoundException("NOT_FOUND", "The required object was not found.",
                        "Request with id=" + userId + " was not found"));
        participationRequest.setStatus(Status.CANCELED);
        return participationRequestMapper.toParticipationRequestDto(participationRequest);
    }

    @Override
    public List<ParticipationRequestDto> getParticipationRequestByEventId(Long userId, Long eventId) {
        getUserOrThrowException(userId);
        getEventOrThrowException(eventId);
        List<ParticipationRequest> participationRequestList = participationRequestRepository.findAlLByEventId(eventId);
        return participationRequestMapper.toParticipationRequestDtoList(participationRequestList);
    }

    @Override
    public EventRequestStatusUpdateResult updateStatusParticipationRequest(Long userId,
                                                                              Long eventId,
                                                                              EventRequestStatusUpdateRequest request) {
        getUserOrThrowException(userId);
        Event event = getEventOrThrowException(eventId);

        List<Long> requestIds = request.getRequestIds();
        List<ParticipationRequest> participationRequestList = participationRequestRepository
                .findAlLByEventId(eventId);
        List<ParticipationRequest> participationRequestWithPendingStatusListForUpdate = participationRequestList.stream()
                .filter(participationRequest -> participationRequest.getStatus() == Status.PENDING && requestIds.contains(participationRequest.getId())).toList();
        List<ParticipationRequest> participationRequestWithConfirmedStatusList = participationRequestList.stream()
                .filter(participationRequest -> participationRequest.getStatus() == Status.CONFIRMED).toList();
        if (requestIds.size() != participationRequestWithPendingStatusListForUpdate.size()) {
            throw new ViolationRestrictException("CONFLICT", "Integrity constraint has been violated.",
                    " Cтатус можно изменить только у заявок, находящихся в состоянии ожидания");
        }
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() < participationRequestWithConfirmedStatusList.size() + requestIds.size()) {
            throw new ViolationRestrictException("CONFLICT", "Integrity constraint has been violated.",
                    " Достигнут лимит по заявкам на данное событие");
        }
        for (ParticipationRequest pr : participationRequestWithPendingStatusListForUpdate) {
            pr.setStatus(request.getStatus());
        }
        List<ParticipationRequest> updatedParticipationRequest = participationRequestRepository.saveAll(participationRequestWithPendingStatusListForUpdate);
        if (participationRequestWithConfirmedStatusList.size() + participationRequestWithPendingStatusListForUpdate.size() == event.getParticipantLimit()) {
            List<ParticipationRequest> remainingParticipationRequestWithPendingStatus = participationRequestList.stream()
                    .filter(participationRequest -> participationRequest.getStatus() == Status.PENDING && !requestIds.contains(participationRequest.getId())).toList();
            if (!remainingParticipationRequestWithPendingStatus.isEmpty()) {
                for (ParticipationRequest pr : remainingParticipationRequestWithPendingStatus) {
                    pr.setStatus(Status.REJECTED);
                }
                participationRequestRepository.saveAll(remainingParticipationRequestWithPendingStatus);
            }
        }

        EventRequestStatusUpdateResult eventRequestStatusUpdateResult = new EventRequestStatusUpdateResult();
        if (request.getStatus() == Status.CONFIRMED) {
            eventRequestStatusUpdateResult
                    .setConfirmedRequests(participationRequestMapper.toParticipationRequestDtoList(updatedParticipationRequest));
        } else {
            eventRequestStatusUpdateResult
                    .setRejectedRequests(participationRequestMapper.toParticipationRequestDtoList(updatedParticipationRequest));
        }
        return eventRequestStatusUpdateResult;
    }

    private User getUserOrThrowException(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("NOT_FOUND", "The required object was not found.",
                        "User with id=" + userId + " was not found"));
    }

    private Event getEventOrThrowException(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("NOT_FOUND", "The required object was not found.",
                        "Event with id=" + eventId + " was not found"));
    }
}
