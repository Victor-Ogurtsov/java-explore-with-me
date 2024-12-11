package ru.practicum.model.comment.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.model.comment.CommentStatus;

@ToString
@Getter
@Setter
public class CommentDto {
    private Long id;
    private String text;
    private Long commentator;
    private Long event;
    private String created;
    private CommentStatus status;
}
