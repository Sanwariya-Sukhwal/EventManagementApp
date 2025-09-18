package com.eventmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.eventmanagement.entity.Event;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findByOrganizerId(Integer organizerId);
    List<Event> findByRegistrationsAttendeeId(Integer attendeeId);
}
