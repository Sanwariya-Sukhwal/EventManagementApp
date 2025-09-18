package com.eventmanagement.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "registrations")

public class Registration {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "registration_date")
    private LocalDate registrationDate;
    
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    
    @ManyToOne
    @JoinColumn(name = "attendee_id")
    private Attendee attendee;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDate getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(LocalDate registrationDate) {
		this.registrationDate = registrationDate;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}
		
	public Attendee getAttendee() {
		return attendee;
	}

	public void setAttendee(Attendee attendee) {
		this.attendee = attendee;
	}
    
    
    
}
