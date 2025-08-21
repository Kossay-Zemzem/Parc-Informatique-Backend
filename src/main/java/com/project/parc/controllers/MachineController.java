package com.project.parc.controllers;

import com.project.parc.models.Machine;
import com.project.parc.models.MachineDTO;
import com.project.parc.services.MachineServ;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void ajouterMachine(@RequestBody Machine m){ //
        machineServ.ajouterMachine(m);
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

    //Delete a machine

    //Update a machine

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
