package com.project.parc.models;

import java.time.LocalDate;

public record MachineExportDTO(
        Integer id,
        String type,
        String marque,
        String modele,
        String serviceTag,
        String reseau,
        String assignedUser,
        String os,
        String cpu,
        Integer ram,
        String typeStockage,
        Integer tailleStockage,
        LocalDate dateAchat,
        LocalDate dateExpirationGarantie,
        String vendeur,
        String commentaire,
        String locationName
) {
    public static MachineExportDTO fromEntity(Machine machine) {
        return new MachineExportDTO(
                machine.getId(),
                machine.getType(),
                machine.getMarque(),
                machine.getModele(),
                machine.getServiceTag(),
                machine.getAssignedUser(),
                machine.getReseau(),
                machine.getOs(),
                machine.getCpu(),
                machine.getRam(),
                machine.getTypeStockage(),
                machine.getTailleStockage(),
                machine.getDateAchat(),
                machine.getDateExpirationGarantie(),
                machine.getVendeur(),
                machine.getCommentaire(),
                machine.getLocation() != null ? machine.getLocation().getName() : ""
        );
    }
}