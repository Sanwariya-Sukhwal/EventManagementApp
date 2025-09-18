package com.eventmanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.eventmanagement.dto.EventDTO;
import com.eventmanagement.entity.Event;
import com.eventmanagement.service.EventService;
import com.eventmanagement.dto.ResponseStructure;

@RestController
@RequestMapping("/events")
@CrossOrigin(origins = "http://127.0.0.1:5503")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping
    public ResponseEntity<ResponseStructure<Event>> saveEvent(@RequestBody EventDTO eventDTO) {
        return eventService.saveEvent(eventDTO);
    }

    @GetMapping
    public ResponseEntity<ResponseStructure<List<Event>>> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseStructure<Event>> getEventById(@PathVariable Integer id) {
        return eventService.getEventById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseStructure<Event>> updateEvent(@PathVariable Integer id, @RequestBody EventDTO eventDTO) {
        return eventService.updateEvent(id, eventDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseStructure<String>> deleteEvent(@PathVariable Integer id) {
        return eventService.deleteEventById(id);
    }

    @GetMapping("/organizer/{organizerId}")
    public ResponseEntity<ResponseStructure<List<Event>>> getEventsByOrganizerId(@PathVariable Integer organizerId) {
        return eventService.getEventsByOrganizerId(organizerId);
    }

    @GetMapping("/attendee/{attendeeId}")
    public ResponseEntity<ResponseStructure<List<Event>>> getEventsByAttendeeId(@PathVariable Integer attendeeId) {
        return eventService.getEventsByAttendeeId(attendeeId);
    }

    @GetMapping("/pagination")
    public ResponseEntity<ResponseStructure<Page<Event>>> getEventsByPaginationAndSorting(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        return eventService.getEventsByPaginationAndSorting(page, size, sortBy);
    }
}
