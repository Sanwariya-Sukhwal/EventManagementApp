package com.eventmanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.eventmanagement.entity.Attendee;
import com.eventmanagement.entity.Registration;
import com.eventmanagement.service.AttendeeService;
import jsp.springboot.dto.ResponseStructure;

@RestController
@RequestMapping("/attendees")
public class AttendeeController {

    @Autowired
    private AttendeeService attendeeService;

    // ✅ Register Attendee
    @PostMapping
    public ResponseEntity<ResponseStructure<Attendee>> registerAttendee(@RequestBody Attendee attendee) {
        return attendeeService.registerAttendee(attendee);
    }

    // ✅ Get All Attendees
    @GetMapping
    public ResponseEntity<ResponseStructure<List<Attendee>>> getAllAttendees() {
        return attendeeService.getAllAttendance();
    }

    // ✅ Get Attendee by ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponseStructure<Attendee>> getAttendeeById(@PathVariable Integer id) {
        return attendeeService.getAttendanceById(id);
    }

    // ✅ Update Attendee
    @PutMapping("/{id}")
    public ResponseEntity<ResponseStructure<Attendee>> updateAttendee(@PathVariable Integer id, @RequestBody Attendee attendee) {
        attendee.setId(id);
        return attendeeService.updateAttendance(attendee);
    }

    // ✅ Delete Attendee
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseStructure<String>> deleteAttendee(@PathVariable Integer id) {
        return attendeeService.deleteAttendance(id);
    }

    // ✅ Get Registrations by Attendee ID
    @GetMapping("/{attendeeId}/registrations")
    public ResponseEntity<ResponseStructure<List<Registration>>> getRegistrationsByAttendee(@PathVariable Integer attendeeId) {
        return attendeeService.getRegistrationByAttendee(attendeeId);
    }

    // ✅ Get Attendee by Contact
    @GetMapping("/contact/{contact}")
    public ResponseEntity<ResponseStructure<List<Attendee>>> getAttendeesByContact(@PathVariable Long contact) {
        return attendeeService.getAttendanceByContact(contact);
    }

    // ✅ Pagination + Sorting
    @GetMapping("/page")
    public ResponseEntity<ResponseStructure<Page<Attendee>>> getPaginatedAttendees(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String field,
            @RequestParam(defaultValue = "asc") String sortOrder) {
        return attendeeService.getAttendanceByPaginationAndSorting(page, size, field, sortOrder);
    }
}
