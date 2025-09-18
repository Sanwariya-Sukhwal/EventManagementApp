package com.eventmanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.eventmanagement.entity.Organizer;
import com.eventmanagement.service.OrganizerService;

import jsp.springboot.dto.ResponseStructure;

@RestController
@RequestMapping("/organizers")
@CrossOrigin(origins = "http://127.0.0.1:5503")
public class OrganizerController {

    @Autowired
    private OrganizerService organizerService;

    
    // Add organizer
    @PostMapping
    public ResponseEntity<ResponseStructure<Organizer>> saveOrganizer(@RequestBody Organizer organizer) {
        return organizerService.saveOrganizer(organizer);
    }
    
     // getAllOrganizer... 
    @GetMapping
    public ResponseEntity<ResponseStructure<List<Organizer>>> getAllOrganizers(){
    	return organizerService.getAllOrganizers();
    }
    
    
    // getOrganizerById...
    @GetMapping("/{id}")
    public ResponseEntity<ResponseStructure<Organizer>> getOrganizerById(@PathVariable Integer id){
    	return organizerService.getOrganizerById(id);
    }
    
    // updateOrganizer... 
    
//    @PutMapping
//    public ResponseEntity<ResponseStructure<Organizer>> updateOrganizer(@RequestBody Organizer organizer){
//    	return organizerService.updateOrganizer(organizer);
//    }
    @PutMapping("/{id}")
    public ResponseEntity<ResponseStructure<Organizer>> updateOrganizer(
            @PathVariable Integer id,
            @RequestBody Organizer organizer) {

        // ensure correct ID is set
        organizer.setId(id);
        return organizerService.updateOrganizer(organizer);
    }

    
    // deleteOrganizer... 
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseStructure<String>> deleteOrganizer(@PathVariable Integer id) {
        return organizerService.deleteOrganizerById(id);
    }

    
    
    // getOrganizerByPaginationAndSorting...
//    @GetMapping("/pagesort/{pageNumber}/{pageSize}/{field}")
//    public ResponseEntity<ResponseStructure<Page<Organizer>>> getOrganizersByPaginationAndSorting(
//            @PathVariable int pageNumber,
//            @PathVariable int pageSize,
//            @PathVariable String field) {
//
//        return organizerService.getOrganizersByPaginationAndSorting(pageNumber, pageSize, field);
//    }
    @GetMapping("/pagesort/{pageNumber}/{pageSize}/{field}/{sortOrder}")
    public ResponseEntity<ResponseStructure<Page<Organizer>>> getOrganizersByPaginationAndSorting(
            @PathVariable int pageNumber,
            @PathVariable int pageSize,
            @PathVariable String field,
            @PathVariable String sortOrder) {

        return organizerService.getOrganizersByPaginationAndSorting(pageNumber, pageSize, field, sortOrder);
    }


    
}
