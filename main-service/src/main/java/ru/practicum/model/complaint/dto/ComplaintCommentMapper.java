package ru.practicum.model.complaint.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.model.comment.dto.CommentMapper;
import ru.practicum.model.complaint.ComplaintComment;
import ru.practicum.model.complaint.StatusComplaint;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = CommentMapper.class)
public interface ComplaintCommentMapper {

    @Mapping(source = "created", target = "created", dateFormat = "yyyy-MM-dd HH:mm:ss")
    ComplaintCommentDto toComplaintCommentDto(ComplaintComment complaintComment);

    String fromStatusComplaint(StatusComplaint statusComplaint);

}
