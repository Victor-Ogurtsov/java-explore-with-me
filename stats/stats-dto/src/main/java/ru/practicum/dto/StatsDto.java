package ru.practicum.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class StatsDto {
    private String app;
    private String uri;
    private Integer hits;
}
