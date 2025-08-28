package com.project.parc.services;

import com.project.parc.models.*;
import com.project.parc.repository.ArchivedHistoryRepository;
import com.project.parc.repository.HistoryRepository;
import com.project.parc.repository.MachineRepository;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class HistoryServ {
    private final HistoryRepository historyRepo;
    private final ArchivedHistoryRepository archivedHistoryRepo;
    private final MachineServ machineServ;
    private static final Logger LG = LogManager.getLogger();

    @Autowired
    public HistoryServ(HistoryRepository HR, MachineServ machineServ, ArchivedHistoryRepository archivedHistoryRepo) {
        this.historyRepo = HR;
        this.machineServ = machineServ;
        this.archivedHistoryRepo = archivedHistoryRepo;
    }

    //Adding history entries ========================
    public HistoryLogDTO ajouterHistoriqueEntry(Integer IDMachine, HistoryLogCreateDTO h) {
        Machine m = machineServ.getMachineById(IDMachine);
        HistoryLog log = new HistoryLog();
        log.setDate(h.getDate());
        log.setDescription(h.getDescription());

        historyRepo.save(log); // Save the history log first to generate an ID
        m.addHistoryLog(log); // Add the history log to the machine (association)
        machineServ.ajouterMachine(m); //updates the machine (instead of adding it since they have the same machineID),this ensures that the history log is persisted in the database with the correct machine association
        //return the created history log
        return new HistoryLogDTO(
                log.getId(),
                log.getDate(),
                log.getDescription()
        );
    }

    //Fetching history entries of a specific meachine
    public Set<HistoryLog> getHistoriqueMachine(Integer IDMachine) {
        return machineServ.getMachineById(IDMachine).getHistoryLogs();
    }

    @Transactional
    public void deleteHistory(Integer machineId, Integer historyId) throws IllegalArgumentException {
        //verify that the history log to be deleted is under the correct machine
        Machine machine = machineServ.getMachineById(machineId);
        // Ensure the collection is loaded
        Set<HistoryLog> logs = machine.getHistoryLogs();
        logs.size(); // Force loading
        HistoryLog historyLog = logs.stream()
                .filter(log -> log.getId().equals(historyId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "[!] ERROR:History log with ID " + historyId + " does not belong to machine with ID " + machineId
                ));
        machine.removeHistoryLog(historyLog); // JPA deletes it from DB automatically

        // Save the machine (orphanRemoval will delete the history log)
        machineServ.ajouterMachine(machine);


        /*
        Set<HistoryLog> historyLogs = machine.getHistoryLogs();
        if (historyLogs.stream().noneMatch(log -> log.getId().equals(historyId))) {
            LG.error("[!] Attempted to delete history log with ID {} that does not belong to machine with ID {}", historyId, machineId);
            throw new IllegalArgumentException("History log with ID " + historyId + " does not belong to machine with ID " + machineId);
        } else {
            historyRepo.deleteById(historyId);
            LG.info("[i] History log with ID {} deleted successfully for machine with ID {}", historyId, machineId);
        }*/
    }

    public List<ArchivedHistoryLog> getArchivedHistoriqueMachine(Integer idMachine) {
        return archivedHistoryRepo.findAllByArchivedMachineId(idMachine);
    }
}
