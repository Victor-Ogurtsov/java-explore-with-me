package ru.practicum.model.complaint;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.model.comment.Comment;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@Entity
@Table(name = "complaints")
public class ComplaintComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "description")
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    private Comment comment;
    @Column(name = "created")
    private LocalDateTime created = LocalDateTime.now();
    @Enumerated(EnumType.STRING)
    private StatusComplaint status = StatusComplaint.PENDING;
}