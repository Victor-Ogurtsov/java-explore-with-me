package ru.practicum.model.comment;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.model.event.Event;
import ru.practicum.model.user.User;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "text")
    private String text;
    @ManyToOne(fetch = FetchType.LAZY)
    private User commentator;
    @ManyToOne(fetch = FetchType.LAZY)
    private Event event;
    @Column(name = "created")
    private LocalDateTime created = LocalDateTime.now();
    @Enumerated(EnumType.STRING)
    private CommentStatus status = CommentStatus.PUBLISHED;
}