package com.project.parc.services;

import com.project.parc.models.MachineExportDTO;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import org.apache.poi.ss.usermodel.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;


@Service
public class ExportMachineServ {
    public ByteArrayInputStream exportToExcel(List<MachineExportDTO> machines) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Machines");

            // Header row
            Row header = sheet.createRow(0);
            String[] columns = {"ID","Type","Brand","Model","ServiceTag","User","OS","CPU","RAM",
                    "Storage Type","Storage Size","Purchase Date","Warranty Expiration",
                    "Vendor","Comment","Location"};
            for (int i = 0; i < columns.length; i++) {
                header.createCell(i).setCellValue(columns[i]);
            }

            int rowIdx = 1;
            for (MachineExportDTO m : machines) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(m.id());
                row.createCell(1).setCellValue(m.type());
                row.createCell(2).setCellValue(m.marque());
                row.createCell(3).setCellValue(m.modele());
                row.createCell(4).setCellValue(m.serviceTag());
                row.createCell(5).setCellValue(m.assignedUser());
                row.createCell(6).setCellValue(m.os());
                row.createCell(7).setCellValue(m.cpu());
                row.createCell(8).setCellValue(m.ram() != null ? m.ram() : 0);
                row.createCell(9).setCellValue(m.typeStockage());
                row.createCell(10).setCellValue(m.tailleStockage() != null ? m.tailleStockage() : 0);
                row.createCell(11).setCellValue(m.dateAchat() != null ? m.dateAchat().toString() : "");
                row.createCell(12).setCellValue(m.dateExpirationGarantie() != null ? m.dateExpirationGarantie().toString() : "");
                row.createCell(13).setCellValue(m.vendeur());
                row.createCell(14).setCellValue(m.commentaire());
                row.createCell(15).setCellValue(m.locationName());
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    public ByteArrayInputStream exportToCsv(List<MachineExportDTO> machines) {
        StringWriter writer = new StringWriter();
        writer.append("ID,Type,Brand,Model,ServiceTag,User,OS,CPU,RAM,StorageType,StorageSize,PurchaseDate,WarrantyExpiration,Vendor,Comment,Location\n");

        for (MachineExportDTO m : machines) {
            writer.append(m.id() + "," + m.type() + "," + m.marque() + "," + m.modele() + "," + m.serviceTag() + "," +
                    m.assignedUser() + "," + m.os() + "," + m.cpu() + "," +
                    (m.ram() != null ? m.ram() : "") + "," +
                    m.typeStockage() + "," + (m.tailleStockage() != null ? m.tailleStockage() : "") + "," +
                    (m.dateAchat() != null ? m.dateAchat().toString() : "") + "," +
                    (m.dateExpirationGarantie() != null ? m.dateExpirationGarantie().toString() : "") + "," +
                    m.vendeur() + "," + m.commentaire() + "," + m.locationName() + "\n");
        }

        return new ByteArrayInputStream(writer.toString().getBytes(StandardCharsets.UTF_8));
    }
}
