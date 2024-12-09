package ru.practicum.service.participant;

import ru.practicum.model.participant.dto.ParticipationRequestDto;
import ru.practicum.model.status.EventRequestStatusUpdateRequest;
import ru.practicum.model.status.EventRequestStatusUpdateResult;


import java.util.List;

public interface ParticipationRequestService {

    ParticipationRequestDto addParticipationRequest(Long userId, Long eventId);

    List<ParticipationRequestDto> getParticipationRequestDtoListForRequester(Long userId);

    ParticipationRequestDto revokeParticipationRequest(Long userId, Long requestId);

    List<ParticipationRequestDto> getParticipationRequestByEventId(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateStatusParticipationRequest(Long userId, Long eventId,
                                                                    EventRequestStatusUpdateRequest request);
}
