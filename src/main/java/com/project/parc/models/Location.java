package com.project.parc.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id; // Identifiant pour la database, sequence

    private String name; // Or 'emplacement' – this replaces the original String field in Machine.
    // You can add more fields here as needed, e.g., address, description, capacity, etc.

    @OneToMany(mappedBy = "location", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Set<Machine> machines = new HashSet<>(); // Bidirectional OneToMany/ManyToOne.
    // Note: I avoided CascadeType.REMOVE and orphanRemoval=true here
    // to prevent accidental deletion of machines when a location is deleted.
    // You can adjust based on your requirements (e.g., set location to null on machines instead).

    // Optional: Helper methods for bidirectional consistency
    public void addMachine(Machine machine) {
        this.machines.add(machine);
        machine.setLocation(this);
    }

    public void removeMachine(Machine machine) {
        this.machines.remove(machine);
        machine.setLocation(null);
    }
}