package com.project.parc.repository;

import com.project.parc.models.Location;
import com.project.parc.models.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location,Integer> {
    boolean existsByName(String name); // Check if a location with the given name already exists

    // Override default findAll with ordering by id ascending
    @Override
    @Query("SELECT l FROM Location l ORDER BY l.id ASC")
    List<Location> findAll();

    boolean existsByNameIgnoreCase(String name); // Check if a location with the given name already exists (case-insensitive)
    Optional<Location> findByNameIgnoreCase(String name); // Find a location by name (case-insensitive)
}
