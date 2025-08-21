package com.project.parc.repository;

import com.project.parc.models.Location;
import com.project.parc.models.Machine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location,Integer> {
    boolean existsByName(String name); // Check if a location with the given name already exists

}
