package com.eventmanagement.dao;

import java.util.List;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

import com.eventmanagement.entity.Attendee;
import com.eventmanagement.entity.Registration;
import com.eventmanagement.repository.AttendeeRepository;
import com.eventmanagement.repository.RegistrationRepository;

@Repository
public class AttendeeDao {

    @Autowired
    private AttendeeRepository attendeeRepository;
    
    @Autowired
    private RegistrationRepository registrationRepository;

    // save ya registerAttendee...
    public Attendee saveAttendee(Attendee attendee) {
        return attendeeRepository.save(attendee);
    }

    // getAllAttendance....
    public List<Attendee> getAllAttendees() {
        return attendeeRepository.findAll();
    }

    // getAttendanceById...
    public Optional<Attendee> getAttendeeById(Integer id) {
        return attendeeRepository.findById(id);
    }

    // updateAttendance... 
    public Attendee updateAttendee(Attendee attendee) {
        return attendeeRepository.save(attendee);
    }
    
    // deleteAttendance...
    public void deleteAttendee(Attendee attendee) {
        attendeeRepository.delete(attendee);
    }
    // ✅ Get Registrations by Attendee ID
    public List<Registration> getRegistrationsByAttendeeId(Integer attendeeId) {
        return registrationRepository.findByAttendeeId(attendeeId); 
    }
    
    // getAttendanceByContact...
    public List<Attendee> getAttendanceByContact(Long contact) {
        return attendeeRepository.findByContact(contact);
    }

    
    
    //getAttendanceByPaginationAndSorting
//    public Page<Attendee> getAttendeesByPaginationAndSorting(int page, int size, String field) {
//        return attendeeRepository.findAll(PageRequest.of(page, size, Sort.by(field).ascending()));
//    }
    public Page<Attendee> getAttendeesByPaginationAndSorting(int page, int size, String field, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("desc")
                ? Sort.by(field).descending()
                : Sort.by(field).ascending();

        Pageable pageable = PageRequest.of(page, size, sort); // ✅ FIXED (no -1)
        return attendeeRepository.findAll(pageable);
    }

}
