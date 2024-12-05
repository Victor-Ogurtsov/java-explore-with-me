package ru.practicum.model.event.update;

import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.model.location.Location;

@Getter
@Setter
@ToString
public class UpdateEventBase {

    @Size(min = 20, max = 2000)
    protected String annotation;
    protected Long category;
    @Size(min = 20, max = 7000)
    protected String description;
    protected String eventDate;
    protected Location location;
    protected Boolean paid;
    @PositiveOrZero
    protected Integer participantLimit;
    protected Boolean requestModeration;
    @Size(min = 3, max = 120)
    protected String title;
}
