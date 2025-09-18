package com.eventmanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.eventmanagement.dto.RegistrationDTO;
import com.eventmanagement.entity.Registration;
import com.eventmanagement.service.RegistrationService;

import jsp.springboot.dto.ResponseStructure;

@RestController
@RequestMapping("/registrations")
@CrossOrigin(origins = "http://127.0.0.1:5503")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @PostMapping
    public ResponseEntity<ResponseStructure<Registration>> saveRegistration(@RequestBody RegistrationDTO dto) {
        return registrationService.saveRegistration(dto);
    }

    @GetMapping
    public ResponseEntity<ResponseStructure<List<Registration>>> getAllRegistrations() {
        return registrationService.getAllRegistrations();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseStructure<Registration>> getRegistrationById(@PathVariable Integer id) {
        return registrationService.getRegistrationById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseStructure<String>> cancelRegistration(@PathVariable Integer id) {
        return registrationService.cancelRegistration(id);
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<ResponseStructure<List<Registration>>> getRegistrationsByEventId(@PathVariable Integer eventId) {
        return registrationService.getRegistrationsByEventId(eventId);
    }

    @GetMapping("/attendee/{attendeeId}")
    public ResponseEntity<ResponseStructure<List<Registration>>> getRegistrationsByAttendeeId(@PathVariable Integer attendeeId) {
        return registrationService.getRegistrationsByAttendeeId(attendeeId);
    }
}
