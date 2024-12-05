package ru.practicum.model.participant.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ParticipationRequestDto {
    Long id;
    String created;
    Long event;
    Long requester;
    String status;
}

