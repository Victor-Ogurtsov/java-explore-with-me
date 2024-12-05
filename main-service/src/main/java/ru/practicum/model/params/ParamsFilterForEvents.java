package ru.practicum.model.params;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.model.state.State;

import java.util.List;

@Getter
@Setter
@ToString
public class ParamsFilterForEvents {
    List<Long> users;
    List<State> states;
    List<Long> categories;
    String rangeStart;
    String rangeEnd;
    Integer from;
    Integer size;
}