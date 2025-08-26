package com.project.parc.config;

import com.project.parc.models.Location;
import com.project.parc.repository.LocationRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocationInitializer {

    private final LocationRepository locationRepository;
    private static final Logger LG = LogManager.getLogger(); // Add a logger for logging purposes using Log4j

    @PostConstruct
    public void ensureSpareLocationExists() {
        String spareLocationName = "SPARE";

        boolean exists = locationRepository.existsByNameIgnoreCase(spareLocationName);

        if (!exists) {
            Location spare = new Location();
            spare.setName(spareLocationName);
            locationRepository.save(spare);
           LG.warn("[i](Location Intitializer): Missing default 'SPARE' location, created automatically.");
        } else {
            LG.info("[i](Location Intitializer): 'SPARE' location already exists.");
        }
    }
}