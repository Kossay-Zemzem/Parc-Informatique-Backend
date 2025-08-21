package com.project.parc.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Machine {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id; // Identifiant pour la database, sequence

    private String type; // Desktop,Laptop, Workstation
    private String marque;
    private String modele;
    private String serviceTag;
    private String reseau;
    private String assignedUser ;
//    private String emplacement;

    // Caractéristiques
    private String os;
    private String cpu;
    private Integer ram; // En Go
    private String typeStockage; // HDD ou SSD
    private Integer tailleStockage; // En Go

    private LocalDate dateAchat;
    private LocalDate dateExpirationGarantie;
    private String vendeur;
    private String commentaire;

    @OneToMany(mappedBy = "machine",cascade = CascadeType.ALL, fetch = FetchType.LAZY,orphanRemoval = true)
    private Set<HistoryLog> historyLogs=new HashSet<>(); //Historique de la machine

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location; // New reference to the Location entity

    //Helper methods to add history logs
    public void addHistoryLog(HistoryLog historyLog) {
        this.historyLogs.add(historyLog);
        historyLog.setMachine(this); // Set the machine reference in the history log
    }
    public void removeHistoryLog(HistoryLog historyLog) {
        historyLogs.remove(historyLog);
        historyLog.setMachine(null); // detach the child
    }

}
