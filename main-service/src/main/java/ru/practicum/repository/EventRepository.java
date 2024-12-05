package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.model.event.Event;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {

    @Query(
            "select e " +
                    "from Event as e " +
                    "JOIN FETCH e.category as c " +
                    "JOIN FETCH e.initiator as i " +
                    "where e.id = ?1 and e.state = 'PUBLISHED' "
    )
    Optional<Event> getPublishedEventById(Long eventId);

    @Query(
            "select e " +
                    "from Event as e " +
                    "JOIN FETCH e.category as c " +
                    "JOIN FETCH e.initiator as i " +
                    "where e.id = ?1 "
    )
    Optional<Event> getEventById(Long eventId);

    @Query(
            "select e " +
                    "from Event as e " +
                    "JOIN FETCH e.category as c " +
                    "JOIN FETCH e.initiator as i " +
                    "where e.id in ?1 "
    )
    List<Event> getEventListByIdList(List<Long> eventIdList);

    @Query(
            "select e " +
                    "from Event as e " +
                    "JOIN FETCH e.category as c " +
                    "JOIN FETCH e.initiator as i " +
                    "where c.id in ?1 "
    )
    List<Event> getEventListByCategoryId(Long catId);
}