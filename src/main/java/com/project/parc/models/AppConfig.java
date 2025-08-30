package com.project.parc.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class AppConfig {
    @Id
    private String config_key;
    private String config_value;
}
