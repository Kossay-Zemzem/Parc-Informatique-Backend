package com.project.parc.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class HistoryLog {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id; // Identifiant pour la database, sequence

    private LocalDate date;
    private String Description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "machine_id")
    private Machine machine;
}
