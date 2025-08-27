package com.project.parc.controllers;

import com.project.parc.models.Machine;
import com.project.parc.models.MachineByIdDTO;
import com.project.parc.models.MachineDTO;
import com.project.parc.services.MachineServ;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MachineController {
    private final MachineServ machineServ;
    private static final Logger LG = LogManager.getLogger(); // Add a logger for logging purposes using Log4j
    @Autowired
    public MachineController(MachineServ MS) {
        this.machineServ = MS;
    }

    //Add machine
    @PostMapping(path = "/machine")
    public MachineDTO ajouterMachine(@RequestBody Machine m){ //
       return machineServ.ajouterMachine(m);
    }

    //Fetch machine by ID
    @GetMapping(path = "/machine/{id}")
    public ResponseEntity<MachineByIdDTO> getMachineById(@PathVariable("id") Integer id) {
        if (id ==null || id <=0){
            LG.warn("[!] Invalid machine ID: {}", id);
            return ResponseEntity.badRequest().body(null);
        }
        LG.info("[i] Fetching machine by id: {}", id);
        return ResponseEntity.ok().body(machineServ.getMachineDTObyId(id));
    }

    //Fetch machines by location
    @GetMapping(path ="/listMachine") //GET MACHINE BY LOCATION
    public List<MachineDTO> getMachinesByEmplacement(@RequestParam("locationId") Integer locationId) {
        LG.info("[i] Fetching machines by location id: {}", locationId);
        return machineServ.getMachinesByEmplacement(locationId);
    }
    //Fetch all machines
    @GetMapping(path = "/parc") // GET ALL MACHINES (ALL LOCATIONS)
    public List<MachineDTO> getAllMachines() {
        LG.info("[i] Fetching all machines from the parc");
        return machineServ.getAllMachines();
    }
    //fetch spare machines (not assigned to any location) CANCELED
//    @GetMapping(path = "/spareMachines")
//    public List<MachineDTO> getSpareMachines() {
//        LG.info("[i] Fetching all spare machines");
//        return machineServ.getSpareMachines();
//    }

    //Update a machine
    @PatchMapping(path = "/machine/{id}")
    public ResponseEntity<MachineDTO> updateMachine(@PathVariable("id") Integer id, @RequestBody Machine updatedMachine) {
        if (id == null || id <= 0) {
            LG.warn("[!] Invalid machine ID: {}", id);
            return ResponseEntity.badRequest().body(null);
        }
        LG.info("[i] Updating machine with id: {}", id);
        MachineDTO machineDTO = machineServ.updateMachine(id, updatedMachine);
        if (machineDTO == null) {
            LG.warn("[!] Machine with ID {} not found for update", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(machineDTO);
    }

    //Delete a machine
    @DeleteMapping(path = "/machine/{id}")
    public ResponseEntity<?> deleteMachine(@PathVariable("id") Integer id) {
        if (id == null || id <= 0) {
            LG.warn("[!] Invalid machine ID: {}", id);
            return ResponseEntity.badRequest().body(null);
        }
        LG.info("[i] Deleting machine with id: {}", id);
        boolean deleted = machineServ.deleteMachine(id);
        if (!deleted) {
            LG.warn("[!] Machine with ID {} not found for deletion", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    //recover list of deleted machines
    @GetMapping(path = "/archivedMachines")
    public List<MachineDTO> getAllArchivedMachines() {
        LG.info("[i] Fetching all deleted (archived) machines");
        return machineServ.getAllArchivedMachines();
    }

    //restore a deleted machine
    @PostMapping(path = "/archivedMachine/{ArchiveId}/restore")
    public ResponseEntity<MachineDTO> restoreArchivedMachine(@PathVariable("ArchiveId") Integer ArchiveId) {
        if (ArchiveId == null || ArchiveId <= 0) {
            LG.warn("[!] Invalid archived machine ID: {}", ArchiveId);
            return ResponseEntity.badRequest().body(null);
        }
        LG.info("[i] Restoring archived machine with ArchiveId: {}", ArchiveId);
        MachineDTO restoredMachine = machineServ.restoreArchivedMachine(ArchiveId);
        if (restoredMachine == null) {
            LG.warn("[!] Archived machine with ID {} not found for restoration", ArchiveId);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(restoredMachine);
    }

    //==============  Broken/unmaintained endpoints (for now) ==============================================
    //Methode is broken for now (JSON infinte reposnse) , need to use jackson annotations to fix it TODO
    @GetMapping(path = "/parc/full")
    public List<Machine> getAllMachinesAndHistory() {
        LG.info("[i] Fetching all machines and history from the parc");
        return machineServ.getAllMachinesAndHistory();
    }

    //ajouter plusieurs machines (DO not use for now)
    @PostMapping(path = "/listeMachines")
    public void ajouterListeMachine(@RequestBody List<Machine> listM){
        machineServ.ajouterListeMachie(listM);
    }


}
