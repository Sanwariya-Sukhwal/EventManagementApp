package com.eventmanagement.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.eventmanagement.dao.OrganizerDao;
import com.eventmanagement.entity.Organizer;
import com.eventmanagement.exception.IdNotFoundException;
import com.eventmanagement.exception.InvalidSortFieldException;

import jsp.springboot.dto.ResponseStructure;
import jsp.springboot.exception.NoRecordFoundException;

@Service
public class OrganizerService {

    @Autowired
    private OrganizerDao organizerDao;

    // Add Organizer 
    public ResponseEntity<ResponseStructure<Organizer>> saveOrganizer(Organizer organizer) {
        Organizer saved = organizerDao.saveOrganizer(organizer);

        ResponseStructure<Organizer> structure = new ResponseStructure<>();
        structure.setStatuscode(HttpStatus.CREATED.value());
        structure.setMessage("Organizer created successfully");
        structure.setData(saved);

        return new ResponseEntity<>(structure, HttpStatus.CREATED);
    }
    
     // getAllOrganizer... 
    
    public ResponseEntity<ResponseStructure<List<Organizer>>> getAllOrganizers(){
    	List<Organizer> organizers=organizerDao.getAllOrganizers();
    	
    	if(organizers.isEmpty()) {
    		throw new NoRecordFoundException("No organizers found");
    	}
    	
    	ResponseStructure<List<Organizer>> structure= new ResponseStructure<List<Organizer>>();
    	structure.setStatuscode(HttpStatus.OK.value());
    	structure.setMessage("Organizers retrieved sucessfully");
    	structure.setData(organizers);
    	
    	return new ResponseEntity<ResponseStructure<List<Organizer>>>(structure,HttpStatus.OK);
    }
    
    // getOrganizerById...
    public ResponseEntity<ResponseStructure<Organizer>> getOrganizerById(Integer id){
    	Optional<Organizer> optional=organizerDao.getOrganizerById(id);
    	
    	if(optional.isEmpty()) {
    		throw new IdNotFoundException("Organizer ID " + id + "Not found");
    	}
    	
    	ResponseStructure<Organizer> structure= new ResponseStructure<Organizer>();
    	structure.setStatuscode(HttpStatus.OK.value());
    	structure.setMessage("Organizers found sucessfully");
    	structure.setData(optional.get());
    	
    	return new ResponseEntity<ResponseStructure<Organizer>>(structure,HttpStatus.OK);
    }
    
    // updateOrganizer... 
    public ResponseEntity<ResponseStructure<Organizer>> updateOrganizer(Organizer organizer) {
        
        Optional<Organizer> optional = organizerDao.getOrganizerById(organizer.getId());

        if (optional.isEmpty()) {
            throw new IdNotFoundException("Organizer ID " + organizer.getId() + " not found for update");
        }

        Organizer updated = organizerDao.updateOrganizer(organizer);

        ResponseStructure<Organizer> structure = new ResponseStructure<>();
        structure.setStatuscode(HttpStatus.OK.value());
        structure.setMessage("Organizer updated successfully");
        structure.setData(updated);

        return new ResponseEntity<>(structure, HttpStatus.OK);
    }

    
    // deleteOrganizer... 
    public ResponseEntity<ResponseStructure<String>> deleteOrganizerById(Integer id) {
        Optional<Organizer> optional = organizerDao.getOrganizerById(id);

        if (optional.isEmpty()) {
            throw new IdNotFoundException("Organizer ID " + id + " not found");
        }

        organizerDao.deleteOrganizer(optional.get());

        ResponseStructure<String> structure = new ResponseStructure<>();
        structure.setStatuscode(HttpStatus.OK.value());
        structure.setMessage("Organizer deleted successfully");
        structure.setData("Organizer with ID " + id + " deleted");

        return new ResponseEntity<>(structure, HttpStatus.OK);
    }


    
    // getOrganizerByPaginationAndSorting...
    
//    public ResponseEntity<ResponseStructure<Page<Organizer>>> getOrganizersByPaginationAndSorting(int pageNumber, int pageSize, String field) {
//        Page<Organizer> organizers = organizerDao.getOrganizersByPaginationAndSorting(pageNumber, pageSize, field);
//
//        if (!organizers.hasContent()) {
//            throw new NoRecordFoundException("No organizers found on page " + pageNumber + " with sort field: " + field);
//        }
//
//        ResponseStructure<Page<Organizer>> structure = new ResponseStructure<>();
//        structure.setStatuscode(HttpStatus.OK.value());
//        structure.setMessage("Organizers retrieved with pagination and sorting");
//        structure.setData(organizers);
//
//        return new ResponseEntity<>(structure, HttpStatus.OK);
//    }
    public ResponseEntity<ResponseStructure<Page<Organizer>>> getOrganizersByPaginationAndSorting(
            int pageNumber, int pageSize, String field, String sortOrder) {

        List<String> allowedFields = Arrays.asList("id", "name", "email", "organization");
        if (!allowedFields.contains(field)) {
            throw new InvalidSortFieldException("Invalid sort field: " + field + ". Allowed: " + allowedFields);
        }

        Page<Organizer> organizers = organizerDao.getOrganizersByPaginationAndSorting(pageNumber, pageSize, field, sortOrder);

        if (!organizers.hasContent()) {
            throw new NoRecordFoundException("No organizers found on page " + pageNumber + " with sort field: " + field);
        }

        ResponseStructure<Page<Organizer>> structure = new ResponseStructure<>();
        structure.setStatuscode(HttpStatus.OK.value());
        structure.setMessage("Organizers retrieved with pagination and sorting (" + sortOrder + ")");
        structure.setData(organizers);

        return new ResponseEntity<>(structure, HttpStatus.OK);
    }


}
