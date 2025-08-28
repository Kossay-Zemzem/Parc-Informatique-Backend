package com.project.parc.repository;

import com.project.parc.models.ArchivedMachine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;

public interface ArchivedMachineRepository extends JpaRepository<ArchivedMachine,Integer> {

    @Override
    @Query("SELECT am FROM ArchivedMachine am ORDER BY am.deletedAt DESC")
    List<ArchivedMachine> findAll();
    // Custom query to find IDs of machines deleted before a certain date
    @Query("SELECT am.id FROM ArchivedMachine am WHERE am.deletedAt < :date")
    List<Integer> findIdsByDeletedAtBefore(LocalDateTime date);
    // Custom query to find the oldest IDs based on the deletedAt timestamp
//    @Query("SELECT am.id FROM ArchivedMachine am ORDER BY am.deletedAt ASC")
//    List<Integer> findOldestIds(int limit);

}
