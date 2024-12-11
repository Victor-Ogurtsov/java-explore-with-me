package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.complaint.ComplaintComment;

import java.util.Optional;

public interface ComplaintCommentRepository extends JpaRepository<ComplaintComment, Long> {

    @Query(
            "select cc " +
                    "from ComplaintComment as cc " +
                    "JOIN FETCH cc.comment as c " +
                    "where cc.id = ?1 "
    )
    Optional<ComplaintComment> getComplaintCommentById(Long complaintId);
}
