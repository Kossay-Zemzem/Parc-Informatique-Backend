package com.project.parc.services;


import com.project.parc.repository.ArchivedMachineRepository;
import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArchiveCleanupService {

    private final ArchivedMachineRepository archivedMachineRepository;
    private static final Logger LG = LogManager.getLogger(); // Add a logger for logging purposes using Log4

    @Autowired
    public ArchiveCleanupService(ArchivedMachineRepository archivedMachineRepository) {
        this.archivedMachineRepository = archivedMachineRepository;
    }

    // Run cleanup on application startup
    @PostConstruct
    public void cleanupOnStartup() {
        cleanupArchivedMachines();
        LG.info("[i](ArchiveCleanupService): Cleanup on startup initiated.");
    }

    // Run cleanup every day at midnight
    @Scheduled(cron = "0 58 23 * * *")
//    @Scheduled(cron = "0 */2 * * * * ")
    public void cleanupArchivedMachines() {
        LG.info("[i](ArchiveCleanupService): Scheduled cleanup task started.");
        int maxArchivedMachines = 3; // Maximum allowed archived machines
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);

        // Delete machines older than one month
        List<Integer> oldMachines = archivedMachineRepository.findIdsByDeletedAtBefore(oneMonthAgo);
        if (!oldMachines.isEmpty()) {
            archivedMachineRepository.deleteAllById(oldMachines);
            LG.info("[i](ArchiveCleanupService): Deleted {} archived machines older than one month.", oldMachines.size());
        }else {
            LG.info("[i](ArchiveCleanupService): No archived machines older than one month found. Nothing to delete.");
        }

        //canceled for now due to complexe database query issues
//        // Check if the table exceeds the maximum size
//        long totalArchivedMachines = archivedMachineRepository.count();
//        if (totalArchivedMachines > maxArchivedMachines) {
//            int excess = (int) (totalArchivedMachines - maxArchivedMachines);
////            List<Integer> excessMachines = archivedMachineRepository.findOldestIds(excess);
//            List<Integer> excessMachines = archivedMachineRepository.findTop3ByOrderByDeletedAtAsc().subList(0, excess);
//            archivedMachineRepository.deleteAllById(excessMachines);
//            LG.info("[i](ArchiveCleanupService): Deleted {} oldest archived machines to maintain the limit of {}.", excess, maxArchivedMachines);
//        }
    }
}
