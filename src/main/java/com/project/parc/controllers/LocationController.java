package com.project.parc.controllers;

import com.project.parc.models.Location;
import com.project.parc.models.LocationDTO;
import com.project.parc.services.HistoryServ;
import com.project.parc.services.LocationServ;
import jakarta.websocket.server.PathParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LocationController {
    private final LocationServ locationServ;
    private static final Logger LG = LogManager.getLogger(); // Add a logger for logging purposes using Log4j

    @Autowired
    public LocationController(LocationServ LS) {
        this.locationServ = LS;
    }

    //Adding a new location
    @PostMapping("/locations")
    public ResponseEntity<LocationDTO> addLocation(@RequestBody String locationName) {
        if (locationName == null || locationName.trim().isEmpty()) {
            LG.warn("[!] Location name is null or empty.");
            return ResponseEntity.badRequest().body(null);
        }
        LocationDTO newLocation = locationServ.addLocation(locationName);
        if (newLocation == null) {
            LG.warn("[!] Location with name '{}' already exists.", locationName);
            return ResponseEntity.status(409).body(null); // Conflict status code
        }
        return ResponseEntity.status(201).body(newLocation);
    }


    //fetching all locations
    @GetMapping("/locations")
    public ResponseEntity<List<LocationDTO>> getAllLocations() {
        List<LocationDTO> locations = locationServ.getAllLocations();
        return ResponseEntity.ok(locations);
    }

    //Updating a location name
    @PostMapping("/locations/{locationID}/update")
    public ResponseEntity<LocationDTO> updateLocationName(@RequestBody String newName, @PathVariable("locationID") Integer locationID) {
        if (newName == null || newName.trim().isEmpty() || locationID == null) {
            LG.warn("[!] Location ID or new name is null or empty.");
            return ResponseEntity.badRequest().body(null);
        }

        if (locationServ.locationExists(newName)) {
            LG.warn("[!] Location with name '{}' already exists.", newName);
            return ResponseEntity.status(409).body(null); // Conflict status code
        }
        return ResponseEntity.ok().body(locationServ.updateLocationName(locationID, newName));
    }

    //Deleting a location

}