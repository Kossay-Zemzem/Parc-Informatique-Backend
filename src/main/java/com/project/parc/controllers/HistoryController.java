package com.project.parc.controllers;

import com.project.parc.models.HistoryLog;
import com.project.parc.models.HistoryLogCreateDTO;
import com.project.parc.models.HistoryLogDTO;
import com.project.parc.models.Machine;
import com.project.parc.services.HistoryServ;
import com.project.parc.services.MachineServ;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
public class HistoryController {
    private final HistoryServ historyServ;
    private static final Logger LG = LogManager.getLogger(); // Add a logger for logging purposes using Log4j

    @Autowired
    public HistoryController(HistoryServ HS) {
        this.historyServ = HS;
    }

    @PostMapping(path = "/machine/{id}/history")
    public ResponseEntity<HistoryLogDTO> ajouterHistoriqueEntry(@PathVariable("id") Integer idMachine, @RequestBody HistoryLogCreateDTO hl) {
        return ResponseEntity.status(201).body(historyServ.ajouterHistoriqueEntry(idMachine, hl));
    }

    /*
        @GetMapping(path = "/machine/{id}/history")
        public ResponseEntity<?> getHistoriqueMachine(@PathVariable("id") Integer idMachine){
            return ResponseEntity.ok(historyServ.getHistoriqueMachine(idMachine));
        } */
    @GetMapping(path = "/machine/{id}/history")
    public ResponseEntity<?> getHistoriqueMachine(@PathVariable("id") Integer idMachine) {
        List<HistoryLogDTO> historyLogDTOs = historyServ.getHistoriqueMachine(idMachine).stream()
                .map(log -> new HistoryLogDTO(
                        log.getId(),
                        log.getDate(),
                        log.getDescription()
                ))
                .toList();
        return ResponseEntity.ok(historyLogDTOs);
    }

        @GetMapping(path = "/archivedMachine/{id}/history")
    public ResponseEntity<?> getArchivedHistoriqueMachine(@PathVariable("id") Integer idMachine) {
        List<HistoryLogDTO> historyLogDTOs = historyServ.getArchivedHistoriqueMachine(idMachine).stream()
                .map(log -> new HistoryLogDTO(
                        log.getId(),
                        log.getDate(),
                        log.getDescription()
                ))
                .toList();
        return ResponseEntity.ok(historyLogDTOs);
    }





    @DeleteMapping("machine/{machineID}/history/{historyId}")
    public ResponseEntity<?> deleteHistory(@PathVariable("machineID") Integer machineId, @PathVariable("historyId") Integer historyId) {
        try {
            historyServ.deleteHistory(machineId, historyId);
//            return ResponseEntity.ok("History log with ID " + historyId + " deleted successfully for machine ID " + machineId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

    }

}
