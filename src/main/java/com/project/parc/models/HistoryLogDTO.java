package com.project.parc.models;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HistoryLogDTO {
    Integer id ;
    LocalDate date ;
    String description ;
}
