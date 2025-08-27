package com.project.parc.repository;

import com.project.parc.models.ArchivedMachine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArchivedMachineRepository extends JpaRepository<ArchivedMachine,Integer> {

    @Override
    @Query("SELECT am FROM ArchivedMachine am ORDER BY am.deletedAt DESC")
    List<ArchivedMachine> findAll();

}
