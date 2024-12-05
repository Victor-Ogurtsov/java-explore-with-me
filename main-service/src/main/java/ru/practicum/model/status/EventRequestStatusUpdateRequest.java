package ru.practicum.model.status;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class EventRequestStatusUpdateRequest {

    List<Long> requestIds;

    Status status;
}
