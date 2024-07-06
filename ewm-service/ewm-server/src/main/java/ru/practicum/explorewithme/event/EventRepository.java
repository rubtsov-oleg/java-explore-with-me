package ru.practicum.explorewithme.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.user.User;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {
    @Query("SELECT e FROM Event e " +
            "WHERE e.state = 'PUBLISHED' " +
            "AND (:rangeStart IS NULL AND :rangeEnd IS NULL " +
            "OR :rangeStart IS NULL AND :rangeEnd IS NOT NULL AND e.eventDate <= cast(:rangeEnd as timestamp) " +
            "OR :rangeStart IS NOT NULL AND :rangeEnd IS NULL AND e.eventDate >= cast(:rangeStart as timestamp) " +
            "OR :rangeStart IS NOT NULL AND :rangeEnd IS NOT NULL " +
            "AND e.eventDate BETWEEN cast(:rangeStart as timestamp) AND cast(:rangeEnd as timestamp))" +
            "AND (:text IS NULL " +
            "OR (UPPER(e.annotation) LIKE UPPER(CONCAT('%', :text, '%')) " +
            "OR UPPER(e.description) LIKE UPPER(CONCAT('%', :text, '%')))) " +
            "AND (:paid IS NULL OR e.paid = :paid) " +
            "AND (:onlyAvailable = FALSE OR e.participantLimit > (SELECT COUNT(r) FROM Request r WHERE r.event = e.id AND r.status = 'CONFIRMED')) " +
            "AND (:categories IS NULL OR e.category.id IN :categories)")
    Page<Event> getAllEvents(
            @Param("text") String text,
            @Param("categories") List<Integer> categories,
            @Param("paid") Boolean paid,
            @Param("rangeStart") String rangeStart,
            @Param("rangeEnd") String rangeEnd,
            @Param("onlyAvailable") boolean onlyAvailable,
            Pageable pageable);

    @Query("SELECT e FROM Event e " +
            "WHERE (:states IS NULL OR cast(e.state as text) IN :states) " +
            "AND (:users IS NULL OR e.initiator.id IN :users) " +
            "AND (:categories IS NULL OR e.category.id IN :categories) " +
            "AND (:rangeStart IS NULL OR e.eventDate >= cast(:rangeStart as timestamp)) " +
            "AND (:rangeEnd IS NULL OR e.eventDate <= cast(:rangeEnd as timestamp)) ")
    Page<Event> findAllEvents(
            List<String> states,
            List<Integer> users,
            List<Integer> categories,
            String rangeStart,
            String rangeEnd,
            Pageable pageable);

    Page<Event> findAllByInitiator(User initiator, Pageable pageable);
}
