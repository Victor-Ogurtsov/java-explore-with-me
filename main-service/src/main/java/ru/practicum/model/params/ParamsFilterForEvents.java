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
    private List<Long> users;
    private List<State> states;
    private List<Long> categories;
    private String rangeStart;
    private String rangeEnd;
    private Integer from;
    private Integer size;
}