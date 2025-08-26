package com.project.parc.repository;

import com.project.parc.models.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

public interface MachineRepository extends JpaRepository<Machine,Integer> {
//    List<Machine> findAllByEmplacement(String emplacement); // Original method to find machines by location name, now replaced with ID
    List<Machine> findAllByLocationId(Integer locationId); // New method to find machines by Location ID

//    List<Machine> findAllByLocationIsNull(); //to be deleted
}
