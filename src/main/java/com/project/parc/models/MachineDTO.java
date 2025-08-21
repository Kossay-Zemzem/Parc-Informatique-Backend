package com.project.parc.models;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
//DTO pour retourner les informations d'une machine sans l'historique (pour eviter de charger trop de données)
public class MachineDTO {
    private Integer id; // Identifiant pour la database, sequence

    private String type; // Desktop,Laptop, Workstation
    private String marque;
    private String modele;
    private String serviceTag;
    private String reseau;
    private String assignedUser;
    //    private String emplacement;
    private Integer locationId; // New field for Location ID
    private String locationName; // Renamed from emplacement for clarity

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
}
