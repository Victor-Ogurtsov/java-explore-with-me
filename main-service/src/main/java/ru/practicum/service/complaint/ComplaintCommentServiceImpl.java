package ru.practicum.service.complaint;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ViolationRestrictException;
import ru.practicum.model.comment.Comment;
import ru.practicum.model.complaint.ComplaintComment;
import ru.practicum.model.complaint.StatusComplaint;
import ru.practicum.model.complaint.dto.ComplaintCommentDto;
import ru.practicum.model.complaint.dto.ComplaintCommentMapper;
import ru.practicum.model.complaint.dto.NewComplaintComment;
import ru.practicum.repository.CommentRepository;
import ru.practicum.repository.ComplaintCommentRepository;

@RequiredArgsConstructor
@Service
public class ComplaintCommentServiceImpl implements ComplaintCommentService {

    private final CommentRepository commentRepository;
    private final ComplaintCommentRepository complaintCommentRepository;
    private final ComplaintCommentMapper complaintCommentMapper;

    @Override
    public ComplaintCommentDto addComplaintComment(Long commentId, NewComplaintComment newComplaintComment) {
        Comment comment = commentRepository.getCommentById(commentId)
                .orElseThrow(() -> new NotFoundException("NOT_FOUND", "The required object was not found.",
                "Comment with id=" + commentId + " was not found"));
        ComplaintComment complaintComment = new ComplaintComment();
        complaintComment.setComment(comment);
        ComplaintComment addedComplaintComment = complaintCommentRepository.save(complaintComment);
        return complaintCommentMapper.toComplaintCommentDto(addedComplaintComment);
    }

    @Override
    public ComplaintCommentDto updateComplaintComment(Long complaintId, StatusComplaint status) {
        ComplaintComment complaint = complaintCommentRepository.getComplaintCommentById(complaintId)
                .orElseThrow(() -> new NotFoundException("NOT_FOUND", "The required object was not found.",
                "ComplaintComment with id=" + complaintId + " was not found"));
        if (status.equals(StatusComplaint.PENDING)) {
            throw new ViolationRestrictException("CONFLICT", "Integrity constraint has been violated.",
                    "Статус жалобы на комментарий должен быть изменен на CONFIRMED или REJECTED");
        }
        complaint.setStatus(status);
        ComplaintComment updatedComplaint = complaintCommentRepository.save(complaint);
        return complaintCommentMapper.toComplaintCommentDto(updatedComplaint);
    }
}
