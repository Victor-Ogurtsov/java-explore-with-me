package ru.practicum.model.status;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.model.participant.dto.ParticipationRequestDto;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString
public class EventRequestStatusUpdateResult {

    List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
    List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
}