package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.comment.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(
            "select c " +
                    "from Comment as c " +
                    "JOIN FETCH c.commentator as u " +
                    "JOIN FETCH c.event as e " +
                    "where c.id = ?1 "
    )
    Optional<Comment> getCommentById(Long commentId);

    @Query(
            "select c " +
                    "from Comment as c " +
                    "JOIN FETCH c.commentator as u " +
                    "JOIN FETCH c.event as e " +
                    "where e.id = ?1 and c.status = 'PUBLISHED' "
    )
    List<Comment> getCommentListByEventIdAndPublishedStatus(Long commentId);
}
