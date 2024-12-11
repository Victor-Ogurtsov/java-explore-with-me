package ru.practicum.service.comment;

import ru.practicum.model.comment.dto.CommentDto;
import ru.practicum.model.comment.dto.NewComment;

import java.util.List;

public interface CommentService {

    CommentDto addComments(Long userId, Long eventId, NewComment newComment);

    CommentDto updateComments(Long userId, Long commentId, NewComment newComment);

    List<CommentDto> getCommentList(Long eventId);

    CommentDto canceledComment(Long commentId);
}
