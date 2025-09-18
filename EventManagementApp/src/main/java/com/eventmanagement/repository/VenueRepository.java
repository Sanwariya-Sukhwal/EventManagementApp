package com.eventmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eventmanagement.entity.Venue;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Integer> {

	List<Venue> findByLocation(String location);

}
