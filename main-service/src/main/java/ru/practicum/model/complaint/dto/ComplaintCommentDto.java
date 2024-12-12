package ru.practicum.model.complaint.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.model.comment.dto.CommentDto;

@ToString
@Getter
@Setter
public class ComplaintCommentDto {
    private Long id;
    private String description;
    private CommentDto comment;
    private String created;
    private String status;
}