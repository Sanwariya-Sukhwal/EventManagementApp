package com.eventmanagement.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.eventmanagement.entity.Event;
import com.eventmanagement.entity.Venue;
import com.eventmanagement.repository.VenueRepository;

@Repository
public class VenueDao {

    @Autowired
    private VenueRepository venueRepository;

    // Add Venue...
    
    public Venue saveVenue(Venue venue) {
        return venueRepository.save(venue);
    }
    
    // GetAllVenue....
    public List<Venue> getAllVenues() {
        return venueRepository.findAll();
    }

    // get venueById
    public Optional<Venue> getVenueById(Integer id) {
        return venueRepository.findById(id);
    }
    
    // update Venue
    public Venue updateVenue(Venue venue) {
        return venueRepository.save(venue);
    }

    // delete Venue
    public void deleteVenue(Venue venue) {
        venueRepository.delete(venue);
    }

    // getEventsByVenueId(Integer venueId)...
    public List<Event> getEventsByVenueId(Integer venueId) {
        Optional<Venue> optional = venueRepository.findById(venueId);
        return optional.map(Venue::getEvents).orElse(Collections.emptyList());
    }

    
    //  getEventsByLocation(String location)...
    
    public List<Event> getEventsByLocation(String location) {
        List<Venue> venues = venueRepository.findByLocation(location);
        List<Event> allEvents = new ArrayList<>();

        for (Venue venue : venues) {
            if (venue.getEvents() != null) {
                allEvents.addAll(venue.getEvents());
            }
        }

        return allEvents;
    }
    
    // getVenueByPaginationAndSorting(int page, int size, String sortBy)...
    
//    public Page<Venue> getVenuesByPaginationAndSorting(int pageNumber, int pageSize, String field) {
//        return venueRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(field).ascending()));
//    }

    public Page<Venue> getVenuesByPaginationAndSorting(int pageNumber, int pageSize, Sort sort) {
        return venueRepository.findAll(PageRequest.of(pageNumber, pageSize, sort));
    }


    
}
