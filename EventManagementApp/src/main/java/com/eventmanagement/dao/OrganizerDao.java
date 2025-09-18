package com.eventmanagement.dao;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.eventmanagement.entity.Organizer;
import com.eventmanagement.exception.InvalidSortFieldException;
import com.eventmanagement.repository.OrganizerRepository;

@Repository
public class OrganizerDao {

    @Autowired
    private OrganizerRepository organizerRepository;

    // Add organizer 
    public Organizer saveOrganizer(Organizer organizer) {
        return organizerRepository.save(organizer);
    }
    
    // getAllOrganizer... 
    
    public List<Organizer> getAllOrganizers(){
    	return organizerRepository.findAll();
    }
    
    // getOrganizerById...
    public Optional<Organizer> getOrganizerById(Integer id){
    	return organizerRepository.findById(id);
    }
    
    // updateOrganizer... 
    public Organizer updateOrganizer(Organizer organizer) {
    	return organizerRepository.save(organizer);
    }
    
    
    // deleteOrganizer... 
    public void deleteOrganizer(Organizer organizer) {
        organizerRepository.delete(organizer);
    }
    
    // getOrganizerByPaginationAndSorting...
//    public Page<Organizer> getOrganizersByPaginationAndSorting(int pageNumber, int pageSize, String field) {
//        return organizerRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(field).ascending()));
//    }
    public Page<Organizer> getOrganizersByPaginationAndSorting(int pageNumber, int pageSize, String field, String sortOrder) {
        List<String> allowedFields = Arrays.asList("id", "name", "email", "organization");
        if (!allowedFields.contains(field)) {
            throw new InvalidSortFieldException("Invalid sort field: " + field + ". Allowed: " + allowedFields);
        }

        Sort sort = sortOrder.equalsIgnoreCase("desc")
                ? Sort.by(field).descending()
                : Sort.by(field).ascending();

        return organizerRepository.findAll(PageRequest.of(pageNumber, pageSize, sort));
    }


    
    
}
