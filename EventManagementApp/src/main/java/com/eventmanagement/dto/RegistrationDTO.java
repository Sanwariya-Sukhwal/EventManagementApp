package com.eventmanagement.dto;

import java.time.LocalDate;

public class RegistrationDTO {

    private LocalDate registrationDate;
    private Integer eventId;
    private Integer attendeeId;

    // Getters and Setters
    public LocalDate getRegistrationDate() {
        return registrationDate;
    }
    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }
    public Integer getEventId() {
        return eventId;
    }
    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }
    public Integer getAttendeeId() {
        return attendeeId;
    }
    public void setAttendeeId(Integer attendeeId) {
        this.attendeeId = attendeeId;
    }
}
