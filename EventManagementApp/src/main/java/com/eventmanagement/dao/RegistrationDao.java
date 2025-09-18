package com.eventmanagement.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eventmanagement.entity.Registration;
import com.eventmanagement.repository.RegistrationRepository;

@Repository
public class RegistrationDao {

    @Autowired
    private RegistrationRepository registrationRepository;

    public Registration saveRegistration(Registration registration) {
        return registrationRepository.save(registration);
    }

    public List<Registration> getAllRegistrations() {
        return registrationRepository.findAll();
    }

    public Optional<Registration> getRegistrationById(Integer id) {
        return registrationRepository.findById(id);
    }

    public void deleteRegistration(Registration registration) {
        registrationRepository.delete(registration);
    }

    public List<Registration> getRegistrationsByEventId(Integer eventId) {
        return registrationRepository.findByEventId(eventId);
    }

    public List<Registration> getRegistrationsByAttendeeId(Integer attendeeId) {
        return registrationRepository.findByAttendeeId(attendeeId);
    }
}
