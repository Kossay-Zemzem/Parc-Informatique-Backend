package com.project.parc.services;

import com.project.parc.models.*;
import com.project.parc.repository.ArchivedHistoryRepository;
import com.project.parc.repository.ArchivedMachineRepository;
import com.project.parc.repository.LocationRepository;
import com.project.parc.repository.MachineRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class MachineServ {

    private final MachineRepository machineRepo;
    private final ArchivedMachineRepository archivedRepo;
    private final ArchivedHistoryRepository archivedHistoryRepo;
    private final LocationRepository locationRepo;


    @Autowired
    public MachineServ(MachineRepository MR, ArchivedMachineRepository AR, ArchivedHistoryRepository AHR, LocationRepository LR) {
        this.machineRepo = MR;
        this.archivedRepo = AR;
        this.archivedHistoryRepo = AHR;
        this.locationRepo = LR; //for use in restoreArchivedMachine
    }

    //Adding data ========================
    public MachineDTO ajouterMachine(Machine m) {
        //save machine and return machineDTO created
        Machine savedMachine = machineRepo.save(m);
        return new MachineDTO(savedMachine.getId(), savedMachine.getType(), savedMachine.getMarque(), savedMachine.getModele(), savedMachine.getServiceTag(),
                savedMachine.getReseau(),
                savedMachine.getAssignedUser(),
                savedMachine.getLocation() != null ? savedMachine.getLocation().getId() : null, // Map Location ID
                savedMachine.getLocation() != null ? savedMachine.getLocation().getName() : null, // Map Location name
                savedMachine.getOs(),
                savedMachine.getCpu(),
                savedMachine.getRam(),
                savedMachine.getTypeStockage(),
                savedMachine.getTailleStockage(),
                savedMachine.getDateAchat(),
                savedMachine.getDateExpirationGarantie(),
                savedMachine.getVendeur(),
                savedMachine.getCommentaire()
        );
    }

    public void ajouterListeMachie(List<Machine> listM) {
        machineRepo.saveAll(listM);
    }

    //Fetching data =======================
    public Machine getMachineById(Integer id) {
        return machineRepo.findById(id).orElse(null);
    }

    public List<Machine> getAllMachinesAndHistory() {
        return machineRepo.findAll();
    }

    public List<MachineDTO> getAllMachines() {
        return machineRepo.findAll().stream()
                .map(
                        m -> new MachineDTO(m.getId(), m.getType(), m.getMarque(), m.getModele(), m.getServiceTag(),
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
                        m -> new MachineDTO(m.getId(), m.getType(), m.getMarque(), m.getModele(), m.getServiceTag(),
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

    public MachineByIdDTO getMachineDTObyId(Integer id) {
//        return machineRepo.findById(id)
//                .map(m -> new MachineByIdDTO(m.getId(), m.getType(),m.getMarque(),m.getModele(),m.getServiceTag(),
//                        m.getReseau(),
//                        m.getAssignedUser(),
//                        m.getLocation() ,// Include full Location object
//                        m.getOs(),
//                        m.getCpu(),
//                        m.getRam(),
//                        m.getTypeStockage(),
//                        m.getTailleStockage(),
//                        m.getDateAchat(),
//                        m.getDateExpirationGarantie(),
//                        m.getVendeur(),
//                        m.getCommentaire()
//                ))
//                .orElse(null);
        return machineRepo.findById(id)
                .map(machine -> {
                    MachineByIdDTO dto = new MachineByIdDTO();
                    dto.setId(machine.getId());
                    dto.setType(machine.getType());
                    dto.setMarque(machine.getMarque());
                    dto.setModele(machine.getModele());
                    dto.setServiceTag(machine.getServiceTag());
                    dto.setReseau(machine.getReseau());
                    dto.setAssignedUser(machine.getAssignedUser());
                    if (machine.getLocation() != null) {
                        LocationDTO location = new LocationDTO();
                        location.setId(machine.getLocation().getId());
                        location.setName(machine.getLocation().getName());
                        dto.setLocation(location); // Set only the required fields
                    }
                    dto.setOs(machine.getOs());
                    dto.setCpu(machine.getCpu());
                    dto.setRam(machine.getRam());
                    dto.setTypeStockage(machine.getTypeStockage());
                    dto.setTailleStockage(machine.getTailleStockage());
                    dto.setDateAchat(machine.getDateAchat());
                    dto.setDateExpirationGarantie(machine.getDateExpirationGarantie());
                    dto.setVendeur(machine.getVendeur());
                    dto.setCommentaire(machine.getCommentaire());
                    return dto;
                })
                .orElse(null);
    }

    // Fetch spare machines (location is null) CANCELED
//    public List<MachineDTO> getSpareMachines() {
//        return machineRepo.findAllByLocationIsNull().stream()
//                .map(
//                        m -> new MachineDTO(m.getId(), m.getType(), m.getMarque(), m.getModele(), m.getServiceTag(),
//                                m.getReseau(),
//                                m.getAssignedUser(),
//                                null, // Location ID is null for spare machines
//                                null, // Location name is null for spare machines
//                                m.getOs(),
//                                m.getCpu(),
//                                m.getRam(),
//                                m.getTypeStockage(),
//                                m.getTailleStockage(),
//                                m.getDateAchat(),
//                                m.getDateExpirationGarantie(),
//                                m.getVendeur(),
//                                m.getCommentaire()
//                        )).toList();
//    }


    public MachineDTO updateMachine(Integer id, Machine updatedMachine) {
        Machine existing = machineRepo.findById(id).orElse(null);
        if (existing == null) {
            return null; // Machine not found
        }

        // Exclude historyLogs from being updated
        updatedMachine.setHistoryLogs(null);

        copyNonNullProperties(updatedMachine, existing);
        Machine saved = machineRepo.save(existing);
        return new MachineDTO(saved.getId(), saved.getType(), saved.getMarque(), saved.getModele(), saved.getServiceTag(),
                saved.getReseau(),
                saved.getAssignedUser(),
                saved.getLocation() != null ? saved.getLocation().getId() : null, // Map Location ID
                saved.getLocation() != null ? saved.getLocation().getName() : null, // Map Location name
                saved.getOs(),
                saved.getCpu(),
                saved.getRam(),
                saved.getTypeStockage(),
                saved.getTailleStockage(),
                saved.getDateAchat(),
                saved.getDateExpirationGarantie(),
                saved.getVendeur(),
                saved.getCommentaire()
        );
    }

    //Deleting/Modifying data =======================

    //Utility / helper methodes  ========================

    public void copyNonNullProperties(Object src, Object target) { //utility method to copy non-null properties from source to target
        String[] nullPropertyNames = Arrays.stream(
                        BeanUtils.getPropertyDescriptors(src.getClass())
                ).map(pd -> {
                    try {
                        Object value = pd.getReadMethod().invoke(src);
                        return (value == null) ? pd.getName() : null;
                    } catch (Exception e) {
                        return null;
                    }
                }).filter(Objects::nonNull)
                .toArray(String[]::new);

        BeanUtils.copyProperties(src, target, nullPropertyNames);
    }

    //TODO refactor code to use this methode to reduce redundant code
    private MachineDTO mapToMachineDTO(Machine machine) {
        return new MachineDTO(
                machine.getId(),
                machine.getType(),
                machine.getMarque(),
                machine.getModele(),
                machine.getServiceTag(),
                machine.getReseau(),
                machine.getAssignedUser(),
                machine.getLocation() != null ? machine.getLocation().getId() : null, // Map Location ID
                machine.getLocation() != null ? machine.getLocation().getName() : null, // Map Location name
                machine.getOs(),
                machine.getCpu(),
                machine.getRam(),
                machine.getTypeStockage(),
                machine.getTailleStockage(),
                machine.getDateAchat(),
                machine.getDateExpirationGarantie(),
                machine.getVendeur(),
                machine.getCommentaire()
        );
    }

    //Delete and recover machines

    public boolean deleteMachine(Integer id) {
        Optional<Machine> optMachine = machineRepo.findById(id);
        if (optMachine.isPresent()) {
            Machine machine = optMachine.get();

            // Archive everything
            ArchivedMachine archived = new ArchivedMachine(machine);
            archivedRepo.save(archived);

            // Remove from main tables
            machineRepo.delete(machine);

            return true;
        }
        return false;

    }

    public List<MachineDTO> getAllArchivedMachines() {
        return archivedRepo.findAll().stream()
                .map(am -> new MachineDTO(am.getId(), am.getType(), am.getMarque(), am.getModele(), am.getServiceTag(),
                        am.getReseau(),
                        am.getAssignedUser(),
                        am.getLocationId(), // Map Location ID
                        am.getLocationName(), // Map Location name
                        am.getOs(),
                        am.getCpu(),
                        am.getRam(),
                        am.getTypeStockage(),
                        am.getTailleStockage(),
                        am.getDateAchat(),
                        am.getDateExpirationGarantie(),
                        am.getVendeur(),
                        am.getCommentaire()
                )).toList();
    }

    public MachineDTO restoreArchivedMachine(Integer archiveId) {
        Optional<ArchivedMachine> optArchived = archivedRepo.findById(archiveId);
        if (optArchived.isPresent()) {
            ArchivedMachine archived = optArchived.get();

            // Restore to main Machine table
            Machine restored = new Machine();
            restored.setType(archived.getType());
            restored.setMarque(archived.getMarque());
            restored.setModele(archived.getModele());
            restored.setServiceTag(archived.getServiceTag());
            restored.setReseau(archived.getReseau());
            restored.setAssignedUser(archived.getAssignedUser());

            // set location to SPARE (guaranteed to exist)
            Optional<Location> optSpare = locationRepo.findByNameIgnoreCase("SPARE");
            if (optSpare.isPresent()) {
                Location spareLocation = new Location();
                spareLocation.setId(optSpare.get().getId());
                spareLocation.setName(optSpare.get().getName());
                restored.setLocation(spareLocation);
            }

            restored.setOs(archived.getOs());
            restored.setCpu(archived.getCpu());
            restored.setRam(archived.getRam());
            restored.setTypeStockage(archived.getTypeStockage());
            restored.setTailleStockage(archived.getTailleStockage());
            restored.setDateAchat(archived.getDateAchat());
            restored.setDateExpirationGarantie(archived.getDateExpirationGarantie());
            restored.setVendeur(archived.getVendeur());
            restored.setCommentaire(archived.getCommentaire());

            //restoring history log from archived history table
            List<ArchivedHistoryLog> archivedHistories = archivedHistoryRepo.findAllByArchivedMachineId(archived.getId());
            for (ArchivedHistoryLog ah : archivedHistories) {
                HistoryLog hl = new HistoryLog();
                hl.setDate(ah.getDate());
                hl.setDescription(ah.getDescription()); // Link back to the restored machine
                restored.addHistoryLog(hl);
            }

            Machine saved = machineRepo.save(restored);


            // Remove from archived table
            archivedRepo.delete(archived);

            return new MachineDTO(saved.getId(), saved.getType(), saved.getMarque(), saved.getModele(), saved.getServiceTag(),
                    saved.getReseau(),
                    saved.getAssignedUser(),
                    saved.getLocation() != null ? saved.getLocation().getId() : null, // Map Location ID
                    saved.getLocation() != null ? saved.getLocation().getName() : null, // Map Location name
                    saved.getOs(),
                    saved.getCpu(),
                    saved.getRam(),
                    saved.getTypeStockage(),
                    saved.getTailleStockage(),
                    saved.getDateAchat(),
                    saved.getDateExpirationGarantie(),
                    saved.getVendeur(),
                    saved.getCommentaire()
            );
        }
        return null;

    }
}
