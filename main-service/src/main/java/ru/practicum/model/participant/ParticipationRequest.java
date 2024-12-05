package ru.practicum.model.participant;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.model.event.Event;
import ru.practicum.model.status.Status;
import ru.practicum.model.user.User;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@Entity
@Table(name = "requests")
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "created")
    LocalDateTime created = LocalDateTime.now();
    @ManyToOne(fetch = FetchType.LAZY)
    Event event;
    @ManyToOne(fetch = FetchType.LAZY)
    User requester;
    @Enumerated(EnumType.STRING)
    Status status = Status.PENDING;
}
