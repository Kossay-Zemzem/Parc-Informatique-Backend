package com.project.parc.repository;

import com.project.parc.models.ArchivedHistoryLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArchivedHistoryRepository extends JpaRepository<ArchivedHistoryLog, Integer> {
    List<ArchivedHistoryLog> findAllByArchivedMachineId(int machineId);
}
