package com.project.parc.models;


import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MachineByIdDTO { //DTO to return in Get by Id methode (because of front end specifications, should be refactored into machineDTO later)

    private Integer id; // Identifiant pour la database, sequence

    private String type; // Desktop,Laptop, Workstation
    private String marque;
    private String modele;
    private String serviceTag;
    private String reseau;
    private String assignedUser;
    private LocationDTO location; // New reference to the Location entity

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
