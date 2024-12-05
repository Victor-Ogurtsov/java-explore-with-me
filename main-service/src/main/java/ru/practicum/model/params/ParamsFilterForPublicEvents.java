package ru.practicum.model.params;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.model.sort.Sort;

import java.util.List;

@Setter
@Getter
@ToString
public class ParamsFilterForPublicEvents {
    private String text;
    private List<Long> categories;
    private Boolean paid;
    private String rangeStart;
    private String rangeEnd;
    Boolean onlyAvailable;
    Sort sort;
    Integer from;
    Integer size;
}