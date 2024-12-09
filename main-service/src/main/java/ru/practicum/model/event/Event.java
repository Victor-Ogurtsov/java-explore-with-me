package ru.practicum.model.event;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.model.state.State;
import ru.practicum.model.category.Category;

import ru.practicum.model.user.User;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "annotation")
    private String annotation;
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;
    @Column(name = "created_on")
    private LocalDateTime createdOn = LocalDateTime.now();
    @Column(name = "description")
    private String description;
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    @ManyToOne(fetch = FetchType.LAZY)
    private User initiator;
    @Column(name = "location_lat")
    private Float locationLat;
    @Column(name = "location_lon")
    private Float locationLon;
    @Column(name = "paid")
    private Boolean paid;
    @Column(name = "participant_limit")
    private Integer participantLimit;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @Column(name = "request_moderation")
    private Boolean requestModeration;
    @Enumerated(EnumType.STRING)
    private State state = State.PENDING;
    @Column(name = "title")
    private String title;
}

