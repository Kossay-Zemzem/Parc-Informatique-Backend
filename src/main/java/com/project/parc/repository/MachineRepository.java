package com.project.parc.repository;

import com.project.parc.models.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MachineRepository extends JpaRepository<Machine,Integer> {
    List<Machine> findAllByEmplacement(String emplacement);
}
