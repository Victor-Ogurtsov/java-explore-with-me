package ru.practicum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long> {

    @Query(
                    "select eh " +
                    "from EndpointHit as eh " +
                    "where eh.timestamp >= ?1 and eh.timestamp <= ?2 "
    )
    List<EndpointHit> getEndpointHitListByStartAndEnd(LocalDateTime start, LocalDateTime end);
}
