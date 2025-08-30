package com.project.parc.controllers;

import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.project.parc.models.Machine;
import com.project.parc.models.MachineExportDTO;
import com.project.parc.repository.MachineRepository;
import com.project.parc.services.ExportMachineServ;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class MachineExportController {

    private final MachineRepository machineRepo;
    private final ExportMachineServ exportService;

    @Autowired
    public MachineExportController(MachineRepository machineRepository, ExportMachineServ exportService) {
        this.machineRepo = machineRepository;
        this.exportService = exportService;
    }

    // Export all machines to Excel
    @GetMapping("/export/machine/excel")
    public ResponseEntity<Resource> exportMachinesToExcel(
            @RequestParam(required = false) Integer locationId
    ) throws IOException {
        List<Machine> machines ;

        if (locationId != null) {
            machines = machineRepo.findAllByLocationId(locationId);
        } else {
            machines = machineRepo.findAllByOrderByLocationNameDesc();
        }
//        if (machines.isEmpty()) { //safeguard against empty export
//            return ResponseEntity.noContent().build();
//        }
        List<MachineExportDTO> dtos = machines.stream().map(MachineExportDTO::fromEntity).toList();

        ByteArrayInputStream in = exportService.exportToExcel(dtos);
        InputStreamResource file = new InputStreamResource(in);

        //Generate timestamp for filename
        String timestamp = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = "parc_export_" +(locationId != null ? "" : "all")+ "_"+ timestamp+ ".xlsx";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(file);
    }

    // Export all machines to CSV
    @GetMapping("/export/machine/csv")
    public ResponseEntity<Resource> exportMachinesToCsv(
            @RequestParam(required = false) Integer locationId
    ) {
        List<Machine> machines ;
        if (locationId != null) {
            machines = machineRepo.findAllByLocationId(locationId);
        } else {
            machines = machineRepo.findAllByOrderByLocationNameDesc();
        }

        // later: filter
        List<MachineExportDTO> dtos = machines.stream().map(MachineExportDTO::fromEntity).toList();

        ByteArrayInputStream in = exportService.exportToCsv(dtos);
        InputStreamResource file = new InputStreamResource(in);

        //Generate timestamp for filename
        String timestamp = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = "parc_export_" +(locationId != null ? "" : "all")+ "_"+ timestamp + ".csv";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(file);
    }

    //Export all machines to PDF
    @GetMapping("/export/machine/pdf")
    public ResponseEntity<Resource> exportMachinesToPdf(
            @RequestParam(required = false) Integer locationId,
            @RequestParam(defaultValue = "A4") String pageSize
    ) {
        List<Machine> machines;

        if (locationId != null) {
            machines = machineRepo.findAllByLocationId(locationId);
        } else {
            machines = machineRepo.findAllByOrderByLocationNameDesc();
        }


        List<MachineExportDTO> dtos = machines.stream().map(MachineExportDTO::fromEntity).toList();

        Rectangle pageFormat = pageSize.equalsIgnoreCase("A3") ? PageSize.A3 : PageSize.A4;
        ByteArrayInputStream in = exportService.exportToPdf(dtos, pageFormat);

        String timestamp = LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        //file name includes the details of the export
        String filename = "parc_export_" + (locationId != null ? "" : "all") + "_" + pageSize.toUpperCase() + "_" + timestamp + ".pdf";

        InputStreamResource file = new InputStreamResource(in);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.APPLICATION_PDF)
                .body(file);
    }

    //---------------------------------------------------------------
    //Export history logs to excel
    @GetMapping("/export/history/excel")
    public ResponseEntity<Resource> exportMachinesToExcel(
    ) throws IOException {
        List<Machine> machines = machineRepo.findAllByOrderByLocationNameDesc();

//        List<MachineExportDTO> dtos = machines.stream().map(MachineExportDTO::fromEntity).toList();

        ByteArrayInputStream in = exportService.exportHistoryToExcel(machines);
        InputStreamResource file = new InputStreamResource(in);

        //Generate timestamp for filename
        String timestamp = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = "historique_export_" + timestamp+ ".xlsx";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(file);
    }

    // Export history logs to CSV
    @GetMapping("/export/history/csv")
    public ResponseEntity<Resource> exportHistoryToCsv() {
        List<Machine> machines  = machineRepo.findAllByOrderByLocationNameDesc();


//        List<MachineExportDTO> dtos = machines.stream().map(MachineExportDTO::fromEntity).toList();

        ByteArrayInputStream in = exportService.exportHistoryToCsv(machines);
        InputStreamResource file = new InputStreamResource(in);

        //Generate timestamp for filename
        String timestamp = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = "historique_export_" + timestamp + ".csv";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(file);
    }

    //Export history logs to PDF
    @GetMapping("/export/history/pdf")
    public ResponseEntity<Resource> exportHistoryToPdf(
            @RequestParam(defaultValue = "A4") String pageSize
    ) {
        List<Machine> machines = machineRepo.findAllByOrderByLocationNameDesc();

        Rectangle pageFormat = pageSize.equalsIgnoreCase("A3") ? PageSize.A3 : PageSize.A4;
        ByteArrayInputStream in = exportService.exportHistoryToPdf(machines, pageFormat);

        String timestamp = LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        //file name includes the details of the export
        String filename = "historique_export_" + pageSize.toUpperCase() + "_" + timestamp + ".pdf";

        InputStreamResource file = new InputStreamResource(in);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.APPLICATION_PDF)
                .body(file);
    }

}

