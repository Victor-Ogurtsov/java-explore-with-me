package ru.practicum.service.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ViolationRestrictException;
import ru.practicum.model.comment.Comment;
import ru.practicum.model.comment.CommentStatus;
import ru.practicum.model.comment.dto.CommentDto;
import ru.practicum.model.comment.dto.CommentMapper;
import ru.practicum.model.comment.dto.NewComment;
import ru.practicum.model.event.Event;
import ru.practicum.model.state.State;
import ru.practicum.model.user.User;
import ru.practicum.repository.CommentRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Override
    public CommentDto addComments(Long userId, Long eventId, NewComment newComment) {
        User user = getUserOrThrowException(userId);
        Event event = getEventOrThrowException(eventId);
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ViolationRestrictException("CONFLICT", "Integrity constraint has been violated.",
                    "Комментарий можно оставить только под опубликованным событием");
        }
        Comment comment = new Comment();
        comment.setText(newComment.getText());
        comment.setCommentator(user);
        comment.setEvent(event);
        Comment addedComment = commentRepository.save(comment);
        return commentMapper.toCommentDto(addedComment);
    }

    @Override
    public CommentDto updateComments(Long userId, Long commentId, NewComment newComment) {
        getUserOrThrowException(userId);
        Comment comment = getCommentOrThrowException(commentId);
        if (comment.getStatus().equals(CommentStatus.CANCELED)) {
            throw new ViolationRestrictException("CONFLICT", "Integrity constraint has been violated.",
                    "Удаленный комментарий редактировать запрещено");
        }
        if (!comment.getCommentator().getId().equals(userId)) {
            throw new ViolationRestrictException("CONFLICT", "Integrity constraint has been violated.",
                    "Возможно редактировать только свой комментарий");
        }
        if (comment.getCreated().plusMinutes(15).isBefore(LocalDateTime.now())) {
            throw new ViolationRestrictException("CONFLICT", "Integrity constraint has been violated.",
                    "Редактирование комментария возможно в течении 15 минут после публикации");
        }
        comment.setText(newComment.getText());
        Comment updatedComment = commentRepository.save(comment);
        return commentMapper.toCommentDto(updatedComment);
    }

    @Override
    public List<CommentDto> getCommentList(Long eventId) {
        getEventOrThrowException(eventId);
        List<Comment> commentList = commentRepository.getCommentListByEventIdAndPublishedStatus(eventId);
        List<Comment> sortCommentList = commentList.stream().sorted(Comparator.comparing(Comment::getCreated)).toList();
        return commentMapper.toCommentDtoList(sortCommentList);
    }

    @Override
    public CommentDto canceledComment(Long commentId) {
        Comment comment = getCommentOrThrowException(commentId);
        if (comment.getStatus().equals(CommentStatus.CANCELED)) {
            throw new ViolationRestrictException("CONFLICT", "Integrity constraint has been violated.",
                    "Комментарий с id = " + commentId + " уже отменен за нарушение правил");
        }
        comment.setStatus(CommentStatus.CANCELED);
        Comment canceledComment = commentRepository.save(comment);
        return commentMapper.toCommentDto(canceledComment);
    }

    private User getUserOrThrowException(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("NOT_FOUND", "The required object was not found.",
                        "User with id=" + userId + " was not found"));
    }

    private Event getEventOrThrowException(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("NOT_FOUND", "The required object was not found.",
                        "Event with id=" + eventId + " was not found"));
    }

    private Comment getCommentOrThrowException(Long commentId) {
        return commentRepository.getCommentById(commentId)
                .orElseThrow(() -> new NotFoundException("NOT_FOUND", "The required object was not found.",
                        "Comment with id=" + commentId + " was not found"));
    }
}
