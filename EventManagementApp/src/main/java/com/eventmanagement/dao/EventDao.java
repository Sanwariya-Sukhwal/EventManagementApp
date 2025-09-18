package com.eventmanagement.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.eventmanagement.entity.Event;
import com.eventmanagement.repository.EventRepository;

@Repository
public class EventDao {

    @Autowired
    private EventRepository eventRepository;

    // 1. Save Event
    public Event saveEvent(Event event) {
        return eventRepository.save(event);
    }

    // 2. Get All Events
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    // 3. Get Event By Id
    public Optional<Event> getEventById(Integer id) {
        return eventRepository.findById(id);
    }

    // 4. Update Event
    public Event updateEvent(Event event) {
        return eventRepository.save(event);
    }

    // 5. Delete Event
    public void deleteEvent(Event event) {
        eventRepository.delete(event);
    }

    // 6. Get Events By AttendeeId
    public List<Event> getEventsByAttendeeId(Integer attendeeId) {
        return eventRepository.findByRegistrationsAttendeeId(attendeeId);
    }

    // 7. Get Events By OrganizerId
    public List<Event> getEventsByOrganizerId(Integer organizerId) {
        return eventRepository.findByOrganizerId(organizerId);
    }

    // 8. Get Events By Pagination and Sorting
    public Page<Event> getEventsByPaginationAndSorting(Pageable pageable) {
        return eventRepository.findAll(pageable);
    }
}
