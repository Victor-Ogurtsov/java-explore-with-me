package ru.practicum.model.participant.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.model.participant.ParticipationRequest;
import ru.practicum.model.status.Status;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ParticipationRequestMapper {

    @Mapping(target = "created", source = "created",
            dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "event", source = "participationRequest.event.id")
    @Mapping(target = "requester", source = "participationRequest.requester.id")
    ParticipationRequestDto toParticipationRequestDto(ParticipationRequest participationRequest);

    String toString(Status status);

    List<ParticipationRequestDto> toParticipationRequestDtoList(List<ParticipationRequest> participationRequestList);
}
