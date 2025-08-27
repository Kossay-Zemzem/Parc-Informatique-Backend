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
public class ArchivedHistoryLog {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private LocalDate date;
    private String Description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "archived_machine_id")
    private ArchivedMachine archivedMachine;

    public ArchivedHistoryLog(HistoryLog historyLog) {
        this.date = historyLog.getDate();
        this.Description = historyLog.getDescription();
    }

}
