package com.project.parc.config;

import com.project.parc.models.AppConfig;
import com.project.parc.models.Location;
import com.project.parc.repository.AppConfigRepository;
import com.project.parc.repository.LocationRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LocationInitializer {

    private final LocationRepository locationRepository;
    private final AppConfigRepository appConfigRepository;
    private static final Logger LG = LogManager.getLogger(); // Add a logger for logging purposes using Log4j

    @PostConstruct
    public void initializeLocations() {
        String initKey = "locations_initialized";

        // Check if initialization has already been performed
        if (appConfigRepository.findById(initKey).isPresent()) {
            LG.info("[i](Location Initializer): Locations already initialized.");
            return;
        }

        // Add default locations
        List<String> defaultLocations = Arrays.asList("SPARE", "Tunis office", "Sfax","Gabes","Tarfa","Bagel","CPF");
        for (String locationName : defaultLocations) {
            if (!locationRepository.existsByNameIgnoreCase(locationName)) {
                Location location = new Location();
                location.setName(locationName);
                locationRepository.save(location);
                LG.info("[i](Location Initializer): Added default location '{}'.", locationName);
            }
        }

        // Mark initialization as complete
        AppConfig config = new AppConfig();
        config.setConfig_key(initKey);
        config.setConfig_value("true");
        appConfigRepository.save(config);

        LG.info("[i](Location Initializer): Default locations initialized.");
    }

    @PostConstruct
    public void ensureSpareLocationExists() {
        String initKey = "locations_initialized";

        // Check if initialization has already been performed
        if (appConfigRepository.findById(initKey).isEmpty()) {
            LG.info("[i](Location Initializer): Initialization not performed yet, skipping 'SPARE' check.");
            return;
        }

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