package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.participant.ParticipationRequest;

import java.util.List;
import java.util.Optional;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {

    @Query(
                    "select p " +
                    "from ParticipationRequest as p " +
                    "JOIN FETCH p.event as e " +
                    "JOIN FETCH p.requester as r " +
                    "where e.id = ?1 "
    )
    List<ParticipationRequest> findAlLByEventId(Long eventId);

    @Query(
                    "select p " +
                    "from ParticipationRequest as p " +
                    "JOIN FETCH p.event as e " +
                    "JOIN FETCH p.requester as r " +
                    "where r.id = ?1 "
    )
    List<ParticipationRequest> findAlLByUserId(Long userId);

    @Query(
                    "select p " +
                    "from ParticipationRequest as p " +
                    "JOIN FETCH p.event as e " +
                    "JOIN FETCH p.requester as r " +
                    "where p.id = ?1 "
    )
    Optional<ParticipationRequest> getByRequestId(Long requestId);

    @Query(
            "select p " +
                    "from ParticipationRequest as p " +
                    "JOIN FETCH p.event as e " +
                    "JOIN FETCH p.requester as r " +
                    "where e.id in ?1 and p.status = 'CONFIRMED' "
    )
    List<ParticipationRequest> findAlLConfirmedParticipationRequestListByEventIdList(List<Long> eventIdList);
}
