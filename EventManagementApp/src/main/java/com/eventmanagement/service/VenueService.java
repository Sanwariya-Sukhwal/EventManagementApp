package com.eventmanagement.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.eventmanagement.dao.VenueDao;
import com.eventmanagement.dto.VenueDTO;
import com.eventmanagement.entity.Event;
import com.eventmanagement.entity.Venue;
import com.eventmanagement.exception.IdNotFoundException;
import com.eventmanagement.exception.InvalidSortFieldException;

import jsp.springboot.dto.ResponseStructure;
import jsp.springboot.exception.NoRecordFoundException;

@Service
public class VenueService {

    @Autowired
    private VenueDao venueDao;

    
    // Add Venue...
    
    public ResponseEntity<ResponseStructure<Venue>> saveVenue(Venue venue) {
        Venue savedVenue = venueDao.saveVenue(venue);

        ResponseStructure<Venue> structure = new ResponseStructure<>();
        structure.setStatuscode(HttpStatus.CREATED.value());
        structure.setMessage("Venue created successfully");
        structure.setData(savedVenue);

        return new ResponseEntity<>(structure, HttpStatus.CREATED);
    }
    
    // GetAllVenue....
    public ResponseEntity<ResponseStructure<List<Venue>>> getAllVenues() {
        List<Venue> venues = venueDao.getAllVenues();

        if (venues.isEmpty()) {
            throw new NoRecordFoundException("No venues found");
        }

        ResponseStructure<List<Venue>> structure = new ResponseStructure<>();
        structure.setStatuscode(HttpStatus.OK.value());
        structure.setMessage("Venues retrieved successfully");
        structure.setData(venues);

        return new ResponseEntity<>(structure, HttpStatus.OK);
    }
    
    // get venueById
    public ResponseEntity<ResponseStructure<Venue>> getVenueById(Integer id) {
        Optional<Venue> optional = venueDao.getVenueById(id);

        if (optional.isEmpty()) {
            throw new IdNotFoundException("Venue ID " + id + " not found");
        }

        ResponseStructure<Venue> structure = new ResponseStructure<>();
        structure.setStatuscode(HttpStatus.OK.value());
        structure.setMessage("Venue Found Successfully");
        structure.setData(optional.get());

        return new ResponseEntity<>(structure, HttpStatus.OK);
    }

    // update Venue
//    public ResponseEntity<ResponseStructure<Venue>> updateVenue(Venue venue) {
//        Optional<Venue> existing = venueDao.getVenueById(venue.getId());
//
//        if (existing.isEmpty()) {
//            throw new IdNotFoundException("Venue ID " + venue.getId() + " not found for update");
//        }
//
//        Venue updatedVenue = venueDao.updateVenue(venue);
//
//        ResponseStructure<Venue> structure = new ResponseStructure<>();
//        structure.setStatuscode(HttpStatus.OK.value());
//        structure.setMessage("Venue Updated Successfully");
//        structure.setData(updatedVenue);
//
//        return new ResponseEntity<>(structure, HttpStatus.OK);
//    }
    
    public ResponseEntity<ResponseStructure<Venue>> updateVenue(int id, VenueDTO venueDTO) {
        ResponseStructure<Venue> response = new ResponseStructure<>();
        Optional<Venue> dbVenue = venueDao.getVenueById(id);

        if (!dbVenue.isPresent()) {
            response.setStatuscode(HttpStatus.NOT_FOUND.value());
            response.setMessage("Venue Not Found with ID: " + id);
            response.setData(null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Venue existingVenue = dbVenue.get();
        existingVenue.setName(venueDTO.getName());
        existingVenue.setLocation(venueDTO.getLocation());
        existingVenue.setCapacity(venueDTO.getCapacity());

        Venue savedVenue = venueDao.saveVenue(existingVenue);

        response.setStatuscode(HttpStatus.OK.value());
        response.setMessage("Venue Updated Successfully");
        response.setData(savedVenue);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // delete Venue
    public ResponseEntity<ResponseStructure<String>> deleteVenueById(Integer id) {
        Optional<Venue> optional = venueDao.getVenueById(id);
        ResponseStructure<String> structure = new ResponseStructure<>();

        if (optional.isPresent()) {
            venueDao.deleteVenue(optional.get());
            structure.setStatuscode(HttpStatus.OK.value());
            structure.setMessage("Venue Deleted Successfully");
            structure.setData("Venue with ID " + id + " deleted");
            return new ResponseEntity<>(structure, HttpStatus.OK);
        } else {
            throw new IdNotFoundException("Venue ID " + id + " not found");
        }
    }

    // getEventsByVenueId(Integer venueId)....
    
    public ResponseEntity<ResponseStructure<List<Event>>> getEventsByVenueId(Integer venueId) {
        List<Event> events = venueDao.getEventsByVenueId(venueId);

        if (events.isEmpty()) {
            throw new NoRecordFoundException("No events found for venue ID: " + venueId);
        }

        ResponseStructure<List<Event>> structure = new ResponseStructure<>();
        structure.setStatuscode(HttpStatus.OK.value());
        structure.setMessage("Events for Venue ID retrieved");
        structure.setData(events);

        return new ResponseEntity<>(structure, HttpStatus.OK);
    }


    //  getEventsByLocation(String location)...
    public ResponseEntity<ResponseStructure<List<Event>>> getEventsByLocation(String location) {
        List<Event> events = venueDao.getEventsByLocation(location);

        if (events.isEmpty()) {
            throw new NoRecordFoundException("No events found for location: " + location);
        }

        ResponseStructure<List<Event>> structure = new ResponseStructure<>();
        structure.setStatuscode(HttpStatus.OK.value());
        structure.setMessage("Events by location retrieved");
        structure.setData(events);

        return new ResponseEntity<>(structure, HttpStatus.OK);
    }

    // getVenueByPaginationAndSorting(int page, int size, String sortBy)...
//    public ResponseEntity<ResponseStructure<Page<Venue>>> getVenuesByPaginationAndSorting(int pageNumber, int pageSize, String field) {
//        Page<Venue> venues = venueDao.getVenuesByPaginationAndSorting(pageNumber, pageSize, field);
//
//        if (!venues.hasContent()) {
//            throw new NoRecordFoundException("No venues found on page " + pageNumber + " with sort field: " + field);
//        }
//
//        ResponseStructure<Page<Venue>> structure = new ResponseStructure<>();
//        structure.setStatuscode(HttpStatus.OK.value());
//        structure.setMessage("Venues retrieved with pagination and sorting");
//        structure.setData(venues);
//
//        return new ResponseEntity<>(structure, HttpStatus.OK);
//    }
    
    public ResponseEntity<ResponseStructure<Page<Venue>>> getVenuesByPaginationAndSorting(
            int pageNumber, int pageSize, String field, String sortOrder) {

        // ✅ Validate sort field
        List<String> allowedFields = Arrays.asList("id", "name", "location", "capacity");
        if (!allowedFields.contains(field)) {
            throw new InvalidSortFieldException(
                    "Invalid sort field: " + field + ". Allowed fields: " + allowedFields
            );
        }

        Sort sort = sortOrder.equalsIgnoreCase("desc")
                ? Sort.by(field).descending()
                : Sort.by(field).ascending();

        Page<Venue> venues = venueDao.getVenuesByPaginationAndSorting(pageNumber, pageSize, sort);

        if (!venues.hasContent()) {
            throw new NoRecordFoundException(
                    "No venues found on page " + pageNumber + " with sort field: " + field
            );
        }

        ResponseStructure<Page<Venue>> structure = new ResponseStructure<>();
        structure.setStatuscode(HttpStatus.OK.value());
        structure.setMessage("Venues retrieved with pagination and sorting (" + sortOrder + ")");
        structure.setData(venues);

        return new ResponseEntity<>(structure, HttpStatus.OK);
    }

}
