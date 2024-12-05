package ru.practicum.model.event.update;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.model.state.StateActionUser;

@Getter
@Setter
@ToString
public class UpdateEventUserRequest extends UpdateEventBase {

    private StateActionUser stateAction;
}
