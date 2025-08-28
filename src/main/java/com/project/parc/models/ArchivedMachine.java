package com.project.parc.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ArchivedMachine {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private String type;
    private String marque;
    private String modele;
    private String serviceTag;
    private String reseau;
    private String assignedUser;
    private String os;
    private String cpu;
    private Integer ram;
    private String typeStockage;
    private Integer tailleStockage;
    private LocalDate dateAchat;
    private LocalDate dateExpirationGarantie;
    private String vendeur;
    private String commentaire;

    private LocalDateTime deletedAt;

    private Integer locationId;
    private String locationName;

    @OneToMany(mappedBy = "archivedMachine", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ArchivedHistoryLog> archivedHistoryLogs = new HashSet<>();

    public ArchivedMachine(Machine machine) {
        this.type = machine.getType();
        this.marque = machine.getMarque();
        this.modele = machine.getModele();
        this.serviceTag = machine.getServiceTag();
        this.reseau = machine.getReseau();
        this.assignedUser = machine.getAssignedUser();
        this.os = machine.getOs();
        this.cpu = machine.getCpu();
        this.ram = machine.getRam();
        this.typeStockage = machine.getTypeStockage();
        this.tailleStockage = machine.getTailleStockage();
        this.dateAchat = machine.getDateAchat();
        this.dateExpirationGarantie = machine.getDateExpirationGarantie();
        this.vendeur = machine.getVendeur();
        this.commentaire = machine.getCommentaire();
        this.deletedAt = LocalDateTime.now();

        // Copy location
        if (machine.getLocation() != null) {
            this.locationId = machine.getLocation().getId();
            this.locationName = machine.getLocation().getName();
        }

        // Copy history logs
        if (machine.getHistoryLogs() != null) {
            for (HistoryLog log : machine.getHistoryLogs()) {
                ArchivedHistoryLog archivedLog = new ArchivedHistoryLog(log);
                archivedLog.setArchivedMachine(this);
                this.archivedHistoryLogs.add(archivedLog);
            }
        }
    }
}