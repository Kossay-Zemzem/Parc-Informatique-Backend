package com.project.parc.services;

import com.project.parc.models.Machine;
import com.project.parc.models.MachineDTO;
import com.project.parc.repository.MachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class MachineServ {

    private final MachineRepository machineRepo;

    @Autowired
    public MachineServ(MachineRepository MR) {
        this.machineRepo = MR;
    }

    //Adding data ========================
    public void ajouterMachine(Machine m) {
        machineRepo.save(m);
    }
    public void ajouterListeMachie(List<Machine> listM){
        machineRepo.saveAll(listM);
    }
    //Fetching data =======================
    public Machine getMachineById(Integer id) {
        return machineRepo.findById(id).orElse(null);
    }

    public List<Machine> getAllMachinesAndHistory(){
        return machineRepo.findAll();
    }
    public List<MachineDTO> getAllMachines(){
        return machineRepo.findAll().stream()
                .map(
                m -> new MachineDTO(m.getId(), m.getType(),m.getMarque(),m.getModele(),m.getServiceTag(),
                        m.getReseau(),
                        m.getAssignedUser(),
                        m.getLocation() != null ? m.getLocation().getId() : null, // Map Location ID
                        m.getLocation() != null ? m.getLocation().getName() : null, // Map Location name
                        m.getOs(),
                        m.getCpu(),
                        m.getRam(),
                        m.getTypeStockage(),
                        m.getTailleStockage(),
                        m.getDateAchat(),
                        m.getDateExpirationGarantie(),
                        m.getVendeur(),
                        m.getCommentaire()
                )).toList();
    }

    public List<MachineDTO> getMachinesByEmplacement(Integer locationId) {
        return machineRepo.findAllByLocationId(locationId).stream()
                .map(
                m -> new MachineDTO(m.getId(), m.getType(),m.getMarque(),m.getModele(),m.getServiceTag(),
                        m.getReseau(),
                        m.getAssignedUser(),
                        m.getLocation() != null ? m.getLocation().getId() : null, // Map Location ID
                        m.getLocation() != null ? m.getLocation().getName() : null, // Map Location name
                        m.getOs(),
                        m.getCpu(),
                        m.getRam(),
                        m.getTypeStockage(),
                        m.getTailleStockage(),
                        m.getDateAchat(),
                        m.getDateExpirationGarantie(),
                        m.getVendeur(),
                        m.getCommentaire()
                )).toList();
    }

    //Deleting/Modifying data =======================

    //other ? ========================
}
