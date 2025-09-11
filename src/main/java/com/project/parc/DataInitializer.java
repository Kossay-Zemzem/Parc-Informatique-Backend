package com.project.parc;


import com.github.javafaker.Faker;
import com.project.parc.models.LocationDTO;
import com.project.parc.models.Machine;
import com.project.parc.models.Location;
import com.project.parc.repository.MachineRepository;
import com.project.parc.services.LocationServ;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
//This class was made for stress testing with 300 random machines, will be deleted later
@Configuration
public class DataInitializer {

    private static final String[] TYPES = {"Desktop", "Laptop", "Workstation"};
    private static final String[] OS = {"Windows 10", "Windows 11", "Ubuntu", "macOS"};
    private static final String[] STORAGE_TYPES = {"HDD", "SSD"};
    private static final String[] MARQUES = {"Dell", "HP", "Lenovo", "Apple", "Asus"};

    @Bean
    CommandLineRunner initDatabase(MachineRepository machineRepository, LocationServ locationServ) {
        return args -> {
//            Faker faker = new Faker();
//            Random random = new Random();
//
//            // Fetch all locations from LocationServ
//            List<LocationDTO> locations = locationServ.getAllLocations();
//            if (locations.isEmpty()) {
//                System.out.println("No locations found in the database. Please ensure locations with IDs 1 to 5 exist.");
//                return;
//            }
//
//            for (int i = 0; i < 300; i++) {
//                Machine machine = new Machine();
//
//                // Basic info
//                machine.setType(TYPES[random.nextInt(TYPES.length)]);
//                machine.setMarque(MARQUES[random.nextInt(MARQUES.length)]);
//                machine.setModele(MARQUES[random.nextInt(MARQUES.length)] + "-" + faker.number().numberBetween(1000, 9999));
//                machine.setCpu("Intel Core i" + (random.nextInt(3) + 5)); // Example: Intel Core i5, i6, i7
//                machine.setServiceTag(faker.regexify("[A-Z0-9]{7}"));
//                machine.setReseau(faker.internet().ipV4Address());
//                machine.setAssignedUser(faker.name().fullName());
//
//                // Characteristics
//                machine.setOs(OS[random.nextInt(OS.length)]);
//                machine.setCpu("Intel Core i" + (random.nextInt(3) + 5)); // Example: Intel Core i5, i6, i7
//                machine.setRam(random.nextInt(4) * 8 + 8); // 8, 16, 24, 32 GB
//                machine.setTypeStockage(STORAGE_TYPES[random.nextInt(STORAGE_TYPES.length)]);
//                machine.setTailleStockage(random.nextInt(4) * 256 + 256); // 256, 512, 768, 1024 GB
//
//                // Dates and vendor
//                machine.setDateAchat(LocalDate.now().minusDays(faker.number().numberBetween(0, 1095)));
//                machine.setDateExpirationGarantie(machine.getDateAchat().plusYears(3));
//                machine.setVendeur(faker.company().name());
//                machine.setCommentaire(faker.lorem().sentence());
//
//                // Assign a random location (ID 1 to 5)
//                LocationDTO randomLocation = locations.get(random.nextInt(Math.min(locations.size(), 5)));
//                Location location = new Location();
//                location.setId(randomLocation.getId());
//                machine.setLocation(location);
//
//                machineRepository.save(machine);
//            }
//            System.out.println("Inserted 300 test machines into the database with random locations (IDs 1-5).");
        };
    }
}