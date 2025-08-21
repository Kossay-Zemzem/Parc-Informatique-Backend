package com.project.parc.repository;


import com.project.parc.models.HistoryLog;
import com.project.parc.models.Machine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<HistoryLog,Integer> {
}
