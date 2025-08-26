package com.project.parc.controllers;

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

    @GetMapping("/export/excel")
    public ResponseEntity<Resource> exportMachinesToExcel(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate afterDate
    ) throws IOException {
        List<Machine> machines = machineRepo.findAll();
        // later: filter by location & afterDate
        List<MachineExportDTO> dtos = machines.stream().map(MachineExportDTO::fromEntity).toList();

        ByteArrayInputStream in = exportService.exportToExcel(dtos);
        InputStreamResource file = new InputStreamResource(in);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=machines.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(file);
    }

    @GetMapping("/export/csv")
    public ResponseEntity<Resource> exportMachinesToCsv(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate afterDate
    ) {
        List<Machine> machines = machineRepo.findAll();
        // later: filter
        List<MachineExportDTO> dtos = machines.stream().map(MachineExportDTO::fromEntity).toList();

        ByteArrayInputStream in = exportService.exportToCsv(dtos);
        InputStreamResource file = new InputStreamResource(in);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=machines.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(file);
    }
}

