package com.project.parc.services;

import com.project.parc.models.HistoryLog;
import com.project.parc.models.Machine;
import com.project.parc.models.MachineExportDTO;
//import org.apache.poi.ss.usermodel.Font;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;


@Service
public class ExportMachineServ {
    private static final Logger LG = LogManager.getLogger(); // Add a logger for logging purposes using Log4


    // Export list of machines to Excel
    public ByteArrayInputStream exportToExcel(List<MachineExportDTO> machines) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Machines");

            // Header row
            Row header = sheet.createRow(0);
            String[] columns = {"ID", "Type", "Marque", "Modèle", "Service Tag", "Réseau", "Assigné à",
                    "Système", "Processeur", "RAM (Go)", "Type de stockage", "Taille stockage (Go)",
                    "Date d'achat", "garantie", "Vendeur", "Commentaire", "Emplacement"};
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
                row.createCell(6).setCellValue(m.reseau());
                row.createCell(7).setCellValue(m.os());
                row.createCell(8).setCellValue(m.cpu());
                row.createCell(9).setCellValue(m.ram() != null ? m.ram() : 0);
                row.createCell(10).setCellValue(m.typeStockage());
                row.createCell(11).setCellValue(m.tailleStockage() != null ? m.tailleStockage() : 0);
                row.createCell(12).setCellValue(m.dateAchat() != null ? m.dateAchat().toString() : "");
                row.createCell(13).setCellValue(m.dateExpirationGarantie() != null ? m.dateExpirationGarantie().toString() : "");
                row.createCell(14).setCellValue(m.vendeur());
                row.createCell(15).setCellValue(m.commentaire());
                row.createCell(16).setCellValue(m.locationName());
            }

            // Auto-size columns for readability
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    // Export list of machines to CSV
    public ByteArrayInputStream exportToCsv(List<MachineExportDTO> machines) {
        StringWriter writer = new StringWriter();
        writer.append("ID,Type,Marque,Modele,Service Tag,Reseau,Assigne a,Systeme,Processeur,RAM (Go),Type de stockage,Taille stockage (Go),Date achat,Garantie,Vendeur,Commentaire,Emplacement\n");

        for (MachineExportDTO m : machines) {
            writer.append(m.id() + "," + m.type() + "," + m.marque() + "," + m.modele() + "," + m.serviceTag() + "," +
                    m.reseau() + "," + m.assignedUser() + "," + m.os() + "," + m.cpu() + "," +
                    (m.ram() != null ? m.ram() : "") + "," +
                    m.typeStockage() + "," + (m.tailleStockage() != null ? m.tailleStockage() : "") + "," +
                    (m.dateAchat() != null ? m.dateAchat().toString() : "") + "," +
                    (m.dateExpirationGarantie() != null ? m.dateExpirationGarantie().toString() : "") + "," +
                    m.vendeur() + "," + m.commentaire() + "," + m.locationName() + "\n");
        }

        return new ByteArrayInputStream(writer.toString().getBytes(StandardCharsets.UTF_8));
    }

    public ByteArrayInputStream exportToPdf(List<MachineExportDTO> machines, Rectangle pageSize) {
        Document document = new Document(pageSize.rotate()); // landscape orientation
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            String dateExport = java.time.LocalDate.now().toString();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Paragraph title = new Paragraph("PARC INFORMATIQUE - " + dateExport, titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            String[] headers = {
                    "Type", "Marque", "Modèle", "Service Tag", "Réseau", "Assigné à",
                    "Système", "Processeur", "RAM (Go)", "Type de stockage", "Taille stockage (Go)",
                    "Date d'achat", "garantie", "Vendeur", "Commentaire", "Emplacement"
            };

            // Adjust widths for A3 vs A4
            float[] columnWidths = pageSize.equals(PageSize.A3) ?
                    new float[]{
                            6f, 7f, 7f, 7f, 7f, 10f,     // A bit tighter for A3
                            7f, 8f, 5f, 6f, 6f,
                            7f, 7f, 7f, 10f, 7f
                    } :
                    new float[]{
                            7f, 8f, 8f, 8f, 8f, 12f,    // Original for A4
                            8f, 10f, 5f, 7f, 6f,
                            8f, 8f, 8f, 12f, 8f
                    };

            PdfPTable table = new PdfPTable(headers.length);
            table.setWidthPercentage(100);
            table.setWidths(columnWidths);

            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8);
            Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 8);

            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, headFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            for (MachineExportDTO m : machines) {
                table.addCell(new Phrase(m.type(), bodyFont));
                table.addCell(new Phrase(m.marque(), bodyFont));
                table.addCell(new Phrase(m.modele(), bodyFont));
                table.addCell(new Phrase(m.serviceTag(), bodyFont));
                table.addCell(new Phrase(m.reseau(), bodyFont));
                table.addCell(new Phrase(m.assignedUser(), bodyFont));
                table.addCell(new Phrase(m.os(), bodyFont));
                table.addCell(new Phrase(m.cpu(), bodyFont));
                table.addCell(new Phrase(m.ram() != null ? String.valueOf(m.ram()) : "", bodyFont));
                table.addCell(new Phrase(m.typeStockage(), bodyFont));
                table.addCell(new Phrase(m.tailleStockage() != null ? String.valueOf(m.tailleStockage()) : "", bodyFont));
                table.addCell(new Phrase(m.dateAchat() != null ? m.dateAchat().toString() : "", bodyFont));
                table.addCell(new Phrase(m.dateExpirationGarantie() != null ? m.dateExpirationGarantie().toString() : "", bodyFont));
                table.addCell(new Phrase(m.vendeur(), bodyFont));
                table.addCell(new Phrase(m.commentaire(), bodyFont));
                table.addCell(new Phrase(m.locationName(), bodyFont));
            }

            document.add(table);
            document.close();

        } catch (DocumentException e) {
            throw new RuntimeException("Erreur lors de l'export PDF", e);
        }

        LG.info("[i] PDF export généré avec les données des machines");
        return new ByteArrayInputStream(out.toByteArray());
    }
    //---------------------------------------------------------------
    //Export machine history logs to Excel

    public ByteArrayInputStream exportHistoryToExcel(List<Machine> machines) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Historique Machines");

            // Header row
            Row header = sheet.createRow(0);
            String[] columns = {
                    "Type", "Marque", "Modèle", "Service Tag", "Réseau", "Assigné à",
                    "Date", "Description"
            };

            for (int i = 0; i < columns.length; i++) {
                header.createCell(i).setCellValue(columns[i]);
            }

            int rowIdx = 1;

            for (Machine m : machines) {
                for (HistoryLog h : m.getHistoryLogs()) {
                    Row row = sheet.createRow(rowIdx++);

                    row.createCell(0).setCellValue(safe(m.getType()));
                    row.createCell(1).setCellValue(safe(m.getMarque()));
                    row.createCell(2).setCellValue(safe(m.getModele()));
                    row.createCell(3).setCellValue(safe(m.getServiceTag()));
                    row.createCell(4).setCellValue(safe(m.getReseau()));
                    row.createCell(5).setCellValue(safe(m.getAssignedUser()));
                    row.createCell(6).setCellValue(h.getDate() != null ? h.getDate().toString() : "");
                    row.createCell(7).setCellValue(safe(h.getDescription()));
                }
            }

            // Auto-size columns for readability
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }


    // Utility to avoid null values
    private String safe(String value) {
        return value != null ? value : "";
    }


    // Export machine history logs to CSV
    public ByteArrayInputStream exportHistoryToCsv(List<Machine> machines) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(out);

        // Header
        writer.println("Type,Marque,Modele,Service Tag,Reseau,Assigne e,Date,Description");

        // Rows
        for (Machine m : machines) {
            for (HistoryLog h : m.getHistoryLogs()) {
                writer.printf(
                        "%s,%s,%s,%s,%s,%s,%s,%s%n",
                        escapeCsv(m.getType()),
                        escapeCsv(m.getMarque()),
                        escapeCsv(m.getModele()),
                        escapeCsv(m.getServiceTag()),
                        escapeCsv(m.getReseau()),
                        escapeCsv(m.getAssignedUser()),
                        h.getDate() != null ? h.getDate().toString() : "",
                        escapeCsv(h.getDescription())
                );
            }
        }

        writer.flush();
        return new ByteArrayInputStream(out.toByteArray());
    }

    // Export machine history logs to PDF
    public ByteArrayInputStream exportHistoryToPdf(List<Machine> machines, Rectangle pageSize) {
        Document document = new Document(pageSize.rotate()); // Landscape
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            String dateExport = java.time.LocalDate.now().toString();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Paragraph title = new Paragraph("HISTORIQUE DES MACHINES - " + dateExport, titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            // Headers
            String[] headers = {
                    "Type", "Marque", "Modèle", "Service Tag", "Réseau", "Assigné à",
                    "Date Historique", "Description"
            };

            float[] columnWidths = {
                    6f, 7f, 7f, 8f, 8f, 10f, 7f, 20f
            };

            PdfPTable table = new PdfPTable(headers.length);
            table.setWidthPercentage(100);
            table.setWidths(columnWidths);

            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8);
            Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 8);

            // Add header row
            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, headFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            // Add data rows
            for (Machine m : machines) {
                for (HistoryLog h : m.getHistoryLogs()) {
                    table.addCell(new Phrase(safe(m.getType()), bodyFont));
                    table.addCell(new Phrase(safe(m.getMarque()), bodyFont));
                    table.addCell(new Phrase(safe(m.getModele()), bodyFont));
                    table.addCell(new Phrase(safe(m.getServiceTag()), bodyFont));
                    table.addCell(new Phrase(safe(m.getReseau()), bodyFont));
                    table.addCell(new Phrase(safe(m.getAssignedUser()), bodyFont));
                    table.addCell(new Phrase(h.getDate() != null ? h.getDate().toString() : "", bodyFont));
                    table.addCell(new Phrase(safe(h.getDescription()), bodyFont));
                }
            }

            document.add(table);
            document.close();

        } catch (DocumentException e) {
            throw new RuntimeException("Erreur lors de l'export PDF", e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    //Helper methodes -------------------------------------
    private String escapeCsv(String value) {// Escapes commas and quotes in CSV values
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}


//    public ByteArrayInputStream exportToCsv(List<MachineExportDTO> machines) {
//        StringWriter writer = new StringWriter();
//        writer.append("ID,Type,Brand,Model,ServiceTag,User,OS,CPU,RAM,StorageType,StorageSize,PurchaseDate,WarrantyExpiration,Vendor,Comment,Location\n");
//
//        for (MachineExportDTO m : machines) {
//            writer.append(m.id() + "," + m.type() + "," + m.marque() + "," + m.modele() + "," + m.serviceTag() + "," +
//                    m.assignedUser() + "," + m.os() + "," + m.cpu() + "," +
//                    (m.ram() != null ? m.ram() : "") + "," +
//                    m.typeStockage() + "," + (m.tailleStockage() != null ? m.tailleStockage() : "") + "," +
//                    (m.dateAchat() != null ? m.dateAchat().toString() : "") + "," +
//                    (m.dateExpirationGarantie() != null ? m.dateExpirationGarantie().toString() : "") + "," +
//                    m.vendeur() + "," + m.commentaire() + "," + m.locationName() + "\n");
//        }
//
//        return new ByteArrayInputStream(writer.toString().getBytes(StandardCharsets.UTF_8));
//    }
//Export list of machines to PDF (using openpdf)
    /*
    public ByteArrayInputStream exportToPdf(List<MachineExportDTO> machines) {
        Document document = new Document(PageSize.A4.rotate()); // landscape
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Paragraph title = new Paragraph("Machines Export", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            // Table with 16 columns (same as Excel)
            PdfPTable table = new PdfPTable(16);
            table.setWidthPercentage(100);

            String[] headers = {"ID","Type","Brand","Model","ServiceTag","User","OS","CPU","RAM",
                    "Storage Type","Storage Size","Purchase Date","Warranty Expiration",
                    "Vendor","Comment","Location"};

            // Header row
            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            // Data rows
            for (MachineExportDTO m : machines) {
                table.addCell(String.valueOf(m.id()));
                table.addCell(m.type());
                table.addCell(m.marque());
                table.addCell(m.modele());
                table.addCell(m.serviceTag());
                table.addCell(m.assignedUser());
                table.addCell(m.os());
                table.addCell(m.cpu());
                table.addCell(m.ram() != null ? String.valueOf(m.ram()) : "");
                table.addCell(m.typeStockage());
                table.addCell(m.tailleStockage() != null ? String.valueOf(m.tailleStockage()) : "");
                table.addCell(m.dateAchat() != null ? m.dateAchat().toString() : "");
                table.addCell(m.dateExpirationGarantie() != null ? m.dateExpirationGarantie().toString() : "");
                table.addCell(m.vendeur());
                table.addCell(m.commentaire());
                table.addCell(m.locationName());
            }

            document.add(table);
            document.close();

        } catch (DocumentException e) {
            throw new RuntimeException("Error exporting to PDF", e);
        }
        LG.info("[i] PDF export generated with all machines");
        return new ByteArrayInputStream(out.toByteArray());
    }
    */
    /*
    public ByteArrayInputStream exportToPdf(List<MachineExportDTO> machines) {
        Document document = new Document(PageSize.A4.rotate()); // landscape
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();
            //Generate date of the export to be used in the title
            String dateExport = java.time.LocalDate.now().toString();
            // Title (French, slightly larger font)
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
//            Paragraph title = new Paragraph("Données des machines du parc informatique", titleFont);
            Paragraph title = new Paragraph("PARC INFORMATIQUE - " + dateExport, titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            // Define the headers in French (no ID)
            String[] headers = {
                    "Type", "Marque", "Modèle", "Service Tag", "Réseau", "Assigné à",
                    "Système", "Processeur", "RAM (Go)", "Type de stockage", "Taille stockage (Go)",
                    "Date d'achat", "garantie", "Vendeur", "Commentaire", "Emplacement"
            };

            // Column widths (in relative percentages)
            float[] columnWidths = {
                    7f, 8f, 8f, 8f, 8f, 12f,     // General info
                    8f, 10f, 5f, 7f, 6f,         // Specs
                    8f, 8f, 8f, 12f, 8f          // Other
            };

            PdfPTable table = new PdfPTable(headers.length);
            table.setWidthPercentage(100);
            table.setWidths(columnWidths);

            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8);
            Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 8);

            // Header row
            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, headFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            // Data rows
            for (MachineExportDTO m : machines) {
                table.addCell(new Phrase(m.type(), bodyFont));
                table.addCell(new Phrase(m.marque(), bodyFont));
                table.addCell(new Phrase(m.modele(), bodyFont));
                table.addCell(new Phrase(m.serviceTag(), bodyFont));
                table.addCell(new Phrase(m.assignedUser(), bodyFont));
                table.addCell(new Phrase(m.reseau(), bodyFont));
                table.addCell(new Phrase(m.os(), bodyFont));
                table.addCell(new Phrase(m.cpu(), bodyFont));
                table.addCell(new Phrase(m.ram() != null ? String.valueOf(m.ram()) : "", bodyFont));
                table.addCell(new Phrase(m.typeStockage(), bodyFont));
                table.addCell(new Phrase(m.tailleStockage() != null ? String.valueOf(m.tailleStockage()) : "", bodyFont));
                table.addCell(new Phrase(m.dateAchat() != null ? m.dateAchat().toString() : "", bodyFont));
                table.addCell(new Phrase(m.dateExpirationGarantie() != null ? m.dateExpirationGarantie().toString() : "", bodyFont));
                table.addCell(new Phrase(m.vendeur(), bodyFont));
                table.addCell(new Phrase(m.commentaire(), bodyFont));
                table.addCell(new Phrase(m.locationName(), bodyFont));
            }

            document.add(table);
            document.close();

        } catch (DocumentException e) {
            throw new RuntimeException("Erreur lors de l'export PDF", e);
        }

        LG.info("[i] PDF export généré avec les données des machines");
        return new ByteArrayInputStream(out.toByteArray());
    }*/