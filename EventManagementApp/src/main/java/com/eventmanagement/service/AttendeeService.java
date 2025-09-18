package com.eventmanagement.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.eventmanagement.dao.AttendeeDao;
import com.eventmanagement.entity.Attendee;
import com.eventmanagement.entity.Registration;
import jsp.springboot.dto.ResponseStructure;

@Service
public class AttendeeService {

    @Autowired
    private AttendeeDao attendeeDao;

    // ✅ Register Attendee
    public ResponseEntity<ResponseStructure<Attendee>> registerAttendee(Attendee attendee) {
        Attendee saved = attendeeDao.saveAttendee(attendee);

        ResponseStructure<Attendee> structure = new ResponseStructure<>();
        structure.setStatuscode(HttpStatus.CREATED.value());
        structure.setMessage("Attendee Registered Successfully");
        structure.setData(saved);

        return new ResponseEntity<>(structure, HttpStatus.CREATED);
    }

    // ✅ Get All Attendees
    public ResponseEntity<ResponseStructure<List<Attendee>>> getAllAttendance() {
        List<Attendee> list = attendeeDao.getAllAttendees();

        ResponseStructure<List<Attendee>> structure = new ResponseStructure<>();
        structure.setStatuscode(HttpStatus.OK.value());
        structure.setMessage("All Attendees Fetched Successfully");
        structure.setData(list);

        return new ResponseEntity<>(structure, HttpStatus.OK);
    }

    // ✅ Get Attendee by ID
    public ResponseEntity<ResponseStructure<Attendee>> getAttendanceById(Integer id) {
        Optional<Attendee> opt = attendeeDao.getAttendeeById(id);

        ResponseStructure<Attendee> structure = new ResponseStructure<>();
        if (opt.isPresent()) {
            structure.setStatuscode(HttpStatus.OK.value());
            structure.setMessage("Attendee Found");
            structure.setData(opt.get());
            return new ResponseEntity<>(structure, HttpStatus.OK);
        } else {
            structure.setStatuscode(HttpStatus.NOT_FOUND.value());
            structure.setMessage("Attendee Not Found with ID: " + id);
            structure.setData(null);
            return new ResponseEntity<>(structure, HttpStatus.NOT_FOUND);
        }
    }

    // ✅ Update Attendee
    public ResponseEntity<ResponseStructure<Attendee>> updateAttendance(Attendee attendee) {
        Optional<Attendee> opt = attendeeDao.getAttendeeById(attendee.getId());

        ResponseStructure<Attendee> structure = new ResponseStructure<>();
        if (opt.isPresent()) {
            Attendee updated = attendeeDao.updateAttendee(attendee);
            structure.setStatuscode(HttpStatus.OK.value());
            structure.setMessage("Attendee Updated Successfully");
            structure.setData(updated);
            return new ResponseEntity<>(structure, HttpStatus.OK);
        } else {
            structure.setStatuscode(HttpStatus.NOT_FOUND.value());
            structure.setMessage("Attendee Not Found with ID: " + attendee.getId());
            structure.setData(null);
            return new ResponseEntity<>(structure, HttpStatus.NOT_FOUND);
        }
    }

    // ✅ Delete Attendee
    public ResponseEntity<ResponseStructure<String>> deleteAttendance(Integer id) {
        Optional<Attendee> opt = attendeeDao.getAttendeeById(id);

        ResponseStructure<String> structure = new ResponseStructure<>();
        if (opt.isPresent()) {
            attendeeDao.deleteAttendee(opt.get());
            structure.setStatuscode(HttpStatus.OK.value());
            structure.setMessage("Attendee Deleted Successfully");
            structure.setData("Deleted ID: " + id);
            return new ResponseEntity<>(structure, HttpStatus.OK);
        } else {
            structure.setStatuscode(HttpStatus.NOT_FOUND.value());
            structure.setMessage("Attendee Not Found with ID: " + id);
            structure.setData(null);
            return new ResponseEntity<>(structure, HttpStatus.NOT_FOUND);
        }
    }

    // ✅ Get Registrations by Attendee ID
    public ResponseEntity<ResponseStructure<List<Registration>>> getRegistrationByAttendee(Integer attendeeId) {
        List<Registration> registrations = attendeeDao.getRegistrationsByAttendeeId(attendeeId);

        ResponseStructure<List<Registration>> structure = new ResponseStructure<>();
        if (!registrations.isEmpty()) {
            structure.setStatuscode(HttpStatus.OK.value());
            structure.setMessage("Registrations Found for Attendee ID: " + attendeeId);
            structure.setData(registrations);
            return new ResponseEntity<>(structure, HttpStatus.OK);
        } else {
            structure.setStatuscode(HttpStatus.NOT_FOUND.value());
            structure.setMessage("No Registrations Found for Attendee ID: " + attendeeId);
            structure.setData(null);
            return new ResponseEntity<>(structure, HttpStatus.NOT_FOUND);
        }
    }

    // ✅ Get Attendees by Contact
    public ResponseEntity<ResponseStructure<List<Attendee>>> getAttendanceByContact(Long contact) {
        List<Attendee> list = attendeeDao.getAttendanceByContact(contact);

        ResponseStructure<List<Attendee>> structure = new ResponseStructure<>();
        if (!list.isEmpty()) {
            structure.setStatuscode(HttpStatus.OK.value());
            structure.setMessage("Attendees Found with Contact: " + contact);
            structure.setData(list);
            return new ResponseEntity<>(structure, HttpStatus.OK);
        } else {
            structure.setStatuscode(HttpStatus.NOT_FOUND.value());
            structure.setMessage("No Attendees Found with Contact: " + contact);
            structure.setData(null);
            return new ResponseEntity<>(structure, HttpStatus.NOT_FOUND);
        }
    }

    // ✅ Pagination + Sorting
    public ResponseEntity<ResponseStructure<Page<Attendee>>> getAttendanceByPaginationAndSorting(int page, int size, String field, String sortOrder) {
        Page<Attendee> result = attendeeDao.getAttendeesByPaginationAndSorting(page, size, field, sortOrder);

        ResponseStructure<Page<Attendee>> structure = new ResponseStructure<>();
        structure.setStatuscode(HttpStatus.OK.value());
        structure.setMessage("Paginated and Sorted Attendees Fetched");
        structure.setData(result);

        return new ResponseEntity<>(structure, HttpStatus.OK);
    }
}
