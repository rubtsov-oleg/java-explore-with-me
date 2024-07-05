package ru.practicum.explorewithme.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Integer> {

    Optional<Request> findByRequesterAndEvent(Integer requesterId, Integer eventId);

    @Query("SELECT COUNT(r) FROM Request r WHERE r.event = :eventId AND r.status = 'CONFIRMED'")
    long countByEventIdAndStatusConfirmed(@Param("eventId") Integer eventId);

    List<Request> findAllByRequester(Integer userId);

    List<Request> findAllByEvent(Integer eventId);

    List<Request> findAllByIdIn(List<Integer> ids);

    List<Request> findAllByEventAndStatus(Integer event, RequestStatus status);
}
