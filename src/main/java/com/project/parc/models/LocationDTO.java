package com.project.parc.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LocationDTO {
    private Integer id; // Identifiant pour la database, sequence

    private String name;
}
