package com.project.parc.models;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HistoryLogCreateDTO {
    LocalDate date ;
    String description ;

}
