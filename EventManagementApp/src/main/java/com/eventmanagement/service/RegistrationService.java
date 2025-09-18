package com.eventmanagement.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.eventmanagement.dao.AttendeeDao;
import com.eventmanagement.dao.EventDao;
import com.eventmanagement.dao.RegistrationDao;
import com.eventmanagement.dto.RegistrationDTO;
import com.eventmanagement.entity.Attendee;
import com.eventmanagement.entity.Event;
import com.eventmanagement.entity.Registration;
import com.eventmanagement.exception.IdNotFoundException;
import com.eventmanagement.exception.NoRecordFoundException;

import jsp.springboot.dto.ResponseStructure;

@Service
public class RegistrationService {

    @Autowired
    private RegistrationDao registrationDao;

    @Autowired
    private EventDao eventDao;

    @Autowired
    private AttendeeDao attendeeDao;

    // Create Registration
    public ResponseEntity<ResponseStructure<Registration>> saveRegistration(RegistrationDTO dto) {
        ResponseStructure<Registration> structure = new ResponseStructure<>();

        // Validate Event
        if (dto.getEventId() == null) {
            structure.setStatuscode(HttpStatus.BAD_REQUEST.value());
            structure.setMessage("Event ID must not be null");
            structure.setData(null);
            return new ResponseEntity<>(structure, HttpStatus.BAD_REQUEST);
        }
        Optional<Event> optionalEvent = eventDao.getEventById(dto.getEventId());
        if (optionalEvent.isEmpty()) {
            structure.setStatuscode(HttpStatus.NOT_FOUND.value());
            structure.setMessage("Event not found with ID: " + dto.getEventId());
            structure.setData(null);
            return new ResponseEntity<>(structure, HttpStatus.NOT_FOUND);
        }

        // Validate Attendee
        if (dto.getAttendeeId() == null) {
            structure.setStatuscode(HttpStatus.BAD_REQUEST.value());
            structure.setMessage("Attendee ID must not be null");
            structure.setData(null);
            return new ResponseEntity<>(structure, HttpStatus.BAD_REQUEST);
        }
        Optional<Attendee> optionalAttendee = attendeeDao.getAttendeeById(dto.getAttendeeId());
        if (optionalAttendee.isEmpty()) {
            structure.setStatuscode(HttpStatus.NOT_FOUND.value());
            structure.setMessage("Attendee not found with ID: " + dto.getAttendeeId());
            structure.setData(null);
            return new ResponseEntity<>(structure, HttpStatus.NOT_FOUND);
        }

        Registration registration = new Registration();
        registration.setRegistrationDate(dto.getRegistrationDate());
        registration.setEvent(optionalEvent.get());
        registration.setAttendee(optionalAttendee.get());

        Registration saved = registrationDao.saveRegistration(registration);
        structure.setStatuscode(HttpStatus.CREATED.value());
        structure.setMessage("Registration created successfully");
        structure.setData(saved);

        return new ResponseEntity<>(structure, HttpStatus.CREATED);
    }

    // Get All Registrations
    public ResponseEntity<ResponseStructure<List<Registration>>> getAllRegistrations() {
        List<Registration> registrations = registrationDao.getAllRegistrations();
        if (registrations.isEmpty()) {
            throw new NoRecordFoundException("No registrations found");
        }

        ResponseStructure<List<Registration>> structure = new ResponseStructure<>();
        structure.setStatuscode(HttpStatus.OK.value());
        structure.setMessage("Registrations retrieved successfully");
        structure.setData(registrations);
        return new ResponseEntity<>(structure, HttpStatus.OK);
    }

    // Get Registration by ID
    public ResponseEntity<ResponseStructure<Registration>> getRegistrationById(Integer id) {
        Optional<Registration> optional = registrationDao.getRegistrationById(id);
        if (optional.isEmpty()) {
            throw new IdNotFoundException("Registration ID " + id + " not found");
        }

        ResponseStructure<Registration> structure = new ResponseStructure<>();
        structure.setStatuscode(HttpStatus.OK.value());
        structure.setMessage("Registration found");
        structure.setData(optional.get());
        return new ResponseEntity<>(structure, HttpStatus.OK);
    }

    // Delete Registration
    public ResponseEntity<ResponseStructure<String>> cancelRegistration(Integer id) {
        Optional<Registration> optional = registrationDao.getRegistrationById(id);
        if (optional.isEmpty()) {
            throw new IdNotFoundException("Registration ID " + id + " not found");
        }
        registrationDao.deleteRegistration(optional.get());

        ResponseStructure<String> structure = new ResponseStructure<>();
        structure.setStatuscode(HttpStatus.OK.value());
        structure.setMessage("Registration canceled successfully");
        structure.setData("Registration ID " + id + " canceled");
        return new ResponseEntity<>(structure, HttpStatus.OK);
    }

    // Get Registrations by Event ID
    public ResponseEntity<ResponseStructure<List<Registration>>> getRegistrationsByEventId(Integer eventId) {
        List<Registration> list = registrationDao.getRegistrationsByEventId(eventId);
        if (list.isEmpty()) {
            throw new NoRecordFoundException("No registrations found for event ID: " + eventId);
        }

        ResponseStructure<List<Registration>> structure = new ResponseStructure<>();
        structure.setStatuscode(HttpStatus.OK.value());
        structure.setMessage("Registrations for event retrieved");
        structure.setData(list);
        return new ResponseEntity<>(structure, HttpStatus.OK);
    }

    // Get Registrations by Attendee ID
    public ResponseEntity<ResponseStructure<List<Registration>>> getRegistrationsByAttendeeId(Integer attendeeId) {
        List<Registration> list = registrationDao.getRegistrationsByAttendeeId(attendeeId);
        if (list.isEmpty()) {
            throw new NoRecordFoundException("No registrations found for attendee ID: " + attendeeId);
        }

        ResponseStructure<List<Registration>> structure = new ResponseStructure<>();
        structure.setStatuscode(HttpStatus.OK.value());
        structure.setMessage("Registrations for attendee retrieved");
        structure.setData(list);
        return new ResponseEntity<>(structure, HttpStatus.OK);
    }
}
