package com.eventmanagement.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.eventmanagement.dao.AttendeeDao;
import com.eventmanagement.dao.EventDao;
import com.eventmanagement.dao.OrganizerDao;
import com.eventmanagement.dao.VenueDao;
import com.eventmanagement.dto.EventDTO;
import com.eventmanagement.dto.ResponseStructure;
import com.eventmanagement.entity.Event;
import com.eventmanagement.entity.Organizer;
import com.eventmanagement.entity.Venue;

@Service
public class EventService {

    @Autowired
    private EventDao eventDao;

    @Autowired
    private VenueDao venueDao;

    @Autowired
    private OrganizerDao organizerDao;

    @Autowired
    private AttendeeDao attendeeDao;


    // ✅ Create Event
    public ResponseEntity<ResponseStructure<Event>> saveEvent(EventDTO eventDTO) {
        ResponseStructure<Event> response = new ResponseStructure<>();

        Optional<Venue> dbVenue = venueDao.getVenueById(eventDTO.getVenueId());
        Optional<Organizer> dbOrganizer = organizerDao.getOrganizerById(eventDTO.getOrganizerId());

        if (!dbVenue.isPresent()) {
            response.setStatuscode(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Invalid Venue ID: " + eventDTO.getVenueId());
            response.setData(null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (!dbOrganizer.isPresent()) {
            response.setStatuscode(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Invalid Organizer ID: " + eventDTO.getOrganizerId());
            response.setData(null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        Event event = new Event();
        event.setTitle(eventDTO.getTitle());
        event.setDescription(eventDTO.getDescription());
        event.setEventDate(eventDTO.getEventDate());
        event.setVenue(dbVenue.get());
        event.setOrganizer(dbOrganizer.get());

        Event savedEvent = eventDao.saveEvent(event);

        response.setStatuscode(HttpStatus.CREATED.value());
        response.setMessage("Event Created Successfully");
        response.setData(savedEvent);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // ✅ Update Event
    public ResponseEntity<ResponseStructure<Event>> updateEvent(int id, EventDTO eventDTO) {
        ResponseStructure<Event> response = new ResponseStructure<>();
        Optional<Event> dbEvent = eventDao.getEventById(id);

        if (!dbEvent.isPresent()) {
            response.setStatuscode(HttpStatus.NOT_FOUND.value());
            response.setMessage("Event Not Found with ID: " + id);
            response.setData(null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Event existingEvent = dbEvent.get();
        existingEvent.setTitle(eventDTO.getTitle());
        existingEvent.setDescription(eventDTO.getDescription());
        existingEvent.setEventDate(eventDTO.getEventDate());

        if (eventDTO.getVenueId() != null) {
            Optional<Venue> dbVenue = venueDao.getVenueById(eventDTO.getVenueId());
            dbVenue.ifPresent(existingEvent::setVenue);
        }

        if (eventDTO.getOrganizerId() != null) {
            Optional<Organizer> dbOrganizer = organizerDao.getOrganizerById(eventDTO.getOrganizerId());
            dbOrganizer.ifPresent(existingEvent::setOrganizer);
        }

        Event savedEvent = eventDao.saveEvent(existingEvent);

        response.setStatuscode(HttpStatus.OK.value());
        response.setMessage("Event Updated Successfully");
        response.setData(savedEvent);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // ✅ Get All Events
    public ResponseEntity<ResponseStructure<List<Event>>> getAllEvents() {
        ResponseStructure<List<Event>> response = new ResponseStructure<>();
        List<Event> events = eventDao.getAllEvents();

        response.setStatuscode(HttpStatus.OK.value());
        response.setMessage("All Events Fetched Successfully");
        response.setData(events);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // ✅ Get Event by ID
    public ResponseEntity<ResponseStructure<Event>> getEventById(int id) {
        ResponseStructure<Event> response = new ResponseStructure<>();
        Optional<Event> dbEvent = eventDao.getEventById(id);

        if (dbEvent.isPresent()) {
            response.setStatuscode(HttpStatus.OK.value());
            response.setMessage("Event Found");
            response.setData(dbEvent.get());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.setStatuscode(HttpStatus.NOT_FOUND.value());
            response.setMessage("Event Not Found with ID: " + id);
            response.setData(null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }


    // ✅ Delete Event
    public ResponseEntity<ResponseStructure<String>> deleteEventById(int id) {
        ResponseStructure<String> response = new ResponseStructure<>();
        Optional<Event> dbEvent = eventDao.getEventById(id);

        if (!dbEvent.isPresent()) {
            response.setStatuscode(HttpStatus.NOT_FOUND.value());
            response.setMessage("Event Not Found with ID: " + id);
            response.setData(null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        eventDao.deleteEvent(dbEvent.get());

        response.setStatuscode(HttpStatus.OK.value());
        response.setMessage("Event Deleted Successfully");
        response.setData("Deleted Event with ID: " + id);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // ✅ Get Events By Organizer ID
    public ResponseEntity<ResponseStructure<List<Event>>> getEventsByOrganizerId(int organizerId) {
        ResponseStructure<List<Event>> response = new ResponseStructure<>();
        List<Event> events = eventDao.getEventsByOrganizerId(organizerId);

        response.setStatuscode(HttpStatus.OK.value());
        response.setMessage("Events By Organizer ID: " + organizerId);
        response.setData(events);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // ✅ Get Events By Attendee ID
    public ResponseEntity<ResponseStructure<List<Event>>> getEventsByAttendeeId(int attendeeId) {
        ResponseStructure<List<Event>> response = new ResponseStructure<>();
        List<Event> events = eventDao.getEventsByAttendeeId(attendeeId);

        response.setStatuscode(HttpStatus.OK.value());
        response.setMessage("Events By Attendee ID: " + attendeeId);
        response.setData(events);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // ✅ Get Events with Pagination + Sorting
    public ResponseEntity<ResponseStructure<Page<Event>>> getEventsByPaginationAndSorting(int page, int size, String sortBy) {
        ResponseStructure<Page<Event>> response = new ResponseStructure<>();
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        Page<Event> events = eventDao.getEventsByPaginationAndSorting(pageable);

        response.setStatuscode(HttpStatus.OK.value());
        response.setMessage("Events Fetched with Pagination and Sorting");
        response.setData(events);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
