package ru.practicum.service.complaint;

import ru.practicum.model.complaint.StatusComplaint;
import ru.practicum.model.complaint.dto.ComplaintCommentDto;
import ru.practicum.model.complaint.dto.NewComplaintComment;

public interface ComplaintCommentService {

    ComplaintCommentDto addComplaintComment(Long commentId, NewComplaintComment newComplaintComment);

    ComplaintCommentDto updateComplaintComment(Long complaintId, StatusComplaint status);
}
