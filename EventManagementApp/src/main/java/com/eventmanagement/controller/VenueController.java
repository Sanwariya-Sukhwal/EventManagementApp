package com.eventmanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.eventmanagement.dto.VenueDTO;
import com.eventmanagement.entity.Event;
import com.eventmanagement.entity.Venue;
import com.eventmanagement.service.VenueService;

import jsp.springboot.dto.ResponseStructure;

@RestController
@RequestMapping("/venues")
@CrossOrigin(origins = "http://127.0.0.1:5503")
public class VenueController {

    @Autowired
    private VenueService venueService;

    // Add Venue...
    @PostMapping
    public ResponseEntity<ResponseStructure<Venue>> saveVenue(@RequestBody Venue venue) {
        return venueService.saveVenue(venue);
    }
    
    // GetAllVenue....
    @GetMapping
    public ResponseEntity<ResponseStructure<List<Venue>>> getAllVenues() {
        return venueService.getAllVenues();
    }

    // get venueById
    @GetMapping("/{id}")
    public ResponseEntity<ResponseStructure<Venue>> getVenueById(@PathVariable Integer id) {
        return venueService.getVenueById(id);
    }
    
    // update Venue
//    @PutMapping
//    public ResponseEntity<ResponseStructure<Venue>> updateVenue(@RequestBody Venue venue) {
//        return venueService.updateVenue(venue);
//    }
    @PutMapping("/{id}")
    public ResponseEntity<ResponseStructure<Venue>> updateVenue(
            @PathVariable Integer id,
            @RequestBody VenueDTO venueDTO) {
        return venueService.updateVenue(id, venueDTO);
    }

    
    
    // delete Venue
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseStructure<String>> deleteVenue(@PathVariable Integer id) {
        return venueService.deleteVenueById(id);
    }
    
    //getEventsByVenueId(Integer venueId)...
    
    @GetMapping("/{venueId}/events")
    public ResponseEntity<ResponseStructure<List<Event>>> getEventsByVenueId(@PathVariable Integer venueId) {
        return venueService.getEventsByVenueId(venueId);
    }
    
    // getEventsByLocation(String location)...
    @GetMapping("/location/{location}/events")
    public ResponseEntity<ResponseStructure<List<Event>>> getEventsByLocation(@PathVariable String location) {
        return venueService.getEventsByLocation(location);
    }
    
    // getVenueByPaginationAndSorting(int page, int size, String sortBy)...
    
//    @GetMapping("/pagesort/{pageNumber}/{pageSize}/{field}")
//    public ResponseEntity<ResponseStructure<Page<Venue>>> getVenuesByPaginationAndSorting(
//            @PathVariable int pageNumber,
//            @PathVariable int pageSize,
//            @PathVariable String field) {
//
//        return venueService.getVenuesByPaginationAndSorting(pageNumber, pageSize, field);
//    }

 // ✅ Venue Pagination & Sorting with field validation and optional sort order
    
    @GetMapping("/pagesort/{pageNumber}/{pageSize}/{field}/{sortOrder}")
    public ResponseEntity<ResponseStructure<Page<Venue>>> getVenuesByPaginationAndSorting(
            @PathVariable int pageNumber,
            @PathVariable int pageSize,
            @PathVariable String field,
            @PathVariable String sortOrder) {

        return venueService.getVenuesByPaginationAndSorting(pageNumber, pageSize, field, sortOrder);
    }

    
}
