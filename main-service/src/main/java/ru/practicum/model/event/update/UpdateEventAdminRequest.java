package ru.practicum.model.event.update;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import ru.practicum.model.state.StateActionAdmin;

@Getter
@Setter
@ToString
public class UpdateEventAdminRequest extends UpdateEventBase {

    private StateActionAdmin stateAction;
}
