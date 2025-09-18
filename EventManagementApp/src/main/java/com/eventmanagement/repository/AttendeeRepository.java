//package com.eventmanagement.repository;
//
//import java.util.List;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//import com.eventmanagement.entity.Attendee;
//import com.eventmanagement.entity.Registration;
//
//@Repository
//public interface AttendeeRepository extends JpaRepository<Attendee, Integer> {
//    List<Attendee> findByContact(Long contact);
//
//	List<Registration> findRegistrationsById(Integer attendeeId);
//
//}

package com.eventmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eventmanagement.entity.Attendee;

@Repository
public interface AttendeeRepository extends JpaRepository<Attendee, Integer> {
    
    List<Attendee> findByContact(Long contact); 
}
