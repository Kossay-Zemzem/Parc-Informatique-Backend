package com.project.parc.services;


import com.project.parc.models.Location;
import com.project.parc.models.LocationDTO;
import com.project.parc.repository.HistoryRepository;
import com.project.parc.repository.LocationRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class LocationServ {
    private final LocationRepository locationRepo;
    private static final Logger LG = LogManager.getLogger();

    @Autowired
    public LocationServ(LocationRepository LR) {
        this.locationRepo = LR;
    }

    //Adding a new location
    public LocationDTO addLocation(String LocationName) throws IllegalArgumentException {
        if (locationRepo.existsByName(LocationName)) {
            LG.warn("[!] Location with name '{}' already exists.", LocationName);
            return null; // Or handle this case differently if needed
        } else {
            LG.info("[i] Adding new location with name: {}", LocationName);
            Location location = new Location();
            location.setName(LocationName);
            locationRepo.save(location);
            return new LocationDTO(location.getId(), location.getName());
        }
    }

    //fetching all locations
    public List<LocationDTO> getAllLocations() {
        LG.info("[i] Fetching all locations.");
        return locationRepo.findAll().stream()
                .map(location -> new LocationDTO(location.getId(), location.getName()))
                .toList();

    }

    //verify that a location exists
    public boolean locationExists(String locationName) {
        if (locationName == null || locationName.trim().isEmpty()) {
            return false;
        }
        boolean exists = locationRepo.existsByName(locationName);
        return exists;
    }

    //Updating a location name
    public LocationDTO updateLocationName(Integer locationId, String newName) {
        if (locationId == null || newName == null || newName.trim().isEmpty()) {
            LG.warn("[!] Location ID or new name is null or empty.");
            return null;
        }
        Location location = locationRepo.findById(locationId).orElse(null);
        if (location == null) {
            LG.warn("[!] Location with ID {} not found.", locationId);
            return null;
        }
        if (locationRepo.existsByName(newName)) {
            LG.warn("[!] Location with name '{}' already exists.", newName);
            return null;
        }
        location.setName(newName);
        locationRepo.save(location);
        return new LocationDTO(location.getId(), location.getName());
    }

    //Deleting a location (TODO ATTENTION for cascading and what to do with associated machines)
    public void deleteLocation(Integer locationID) {

        if (locationID == null) {
            LG.warn("[!] Location ID is null.");
            return;
        }
        Location location = locationRepo.findById(locationID).orElse(null);
        if (location == null) {
            LG.warn("[!] Location with ID {} not found.", locationID);
            throw new IllegalArgumentException("Location not found");
        }
        //safeguard for reserved name "SPARE"
        if (location.getName().equalsIgnoreCase("SPARE")) {
            LG.warn("[!] Cannot delete reserved location 'SPARE'.");
            throw new IllegalArgumentException("Cannot delete reserved location 'SPARE'");
        }

        //check if location has associated machines and move them to "SPARE" location
        if (!location.getMachines().isEmpty()) {
            Location spareLocation = locationRepo.findByNameIgnoreCase("SPARE").orElse(null);
            if (spareLocation == null) { //realistically , this should never happen due to the location initializer
                LG.error("[!] 'SPARE' location not found. Cannot move machines.");
                throw new IllegalStateException("'SPARE' location not found");
            }
            location.getMachines().forEach(machine -> machine.setLocation(spareLocation));
            LG.info("[i] Moved  machines 'SPARE' before deletion.");
        }
        locationRepo.delete(location);
        LG.info("[i] Deleted location with ID: {}", locationID);
    }
}
