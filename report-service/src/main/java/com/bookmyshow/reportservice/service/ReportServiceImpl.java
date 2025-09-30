package com.bookmyshow.reportservice.service;

import com.bookmyshow.reportservice.dto.BookingStatsDto;
import com.bookmyshow.reportservice.dto.RevenueStatsDto;
import com.bookmyshow.reportservice.repository.ReportRepository;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    private static final Logger log = LoggerFactory.getLogger(ReportServiceImpl.class);

    @Autowired
    private ReportRepository reportRepository;

    @Override
    public ByteArrayInputStream generateBookingStatsExcelReport() {
        log.info("Generating Booking Stats Excel report");
        List<BookingStatsDto> stats = reportRepository.getBookingStatistics();

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Booking Statistics");

            // Header
            String[] headers = {"Movie Name", "Total Bookings", "Tickets Sold"};
            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < headers.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(headers[col]);
            }

            // Data
            int rowIdx = 1;
            for (BookingStatsDto stat : stats) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(stat.getMovieName());
                row.createCell(1).setCellValue(stat.getTotalBookings());
                row.createCell(2).setCellValue(stat.getTicketsSold());
            }

            workbook.write(out);
            log.info("Successfully generated Booking Stats Excel report");
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            log.error("Error while generating Booking Stats Excel report", e);
            throw new RuntimeException("Failed to generate Excel report: " + e.getMessage());
        }
    }

    @Override
    public ByteArrayInputStream generateBookingStatsPdfReport() {
        log.info("Generating Booking Stats PDF report");
        List<BookingStatsDto> stats = reportRepository.getBookingStatistics();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try (PdfWriter writer = new PdfWriter(out);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            document.add(new Paragraph("Booking Statistics Report").setBold().setFontSize(18).setTextAlignment(TextAlignment.CENTER));

            Table table = new Table(UnitValue.createPercentArray(new float[]{3, 2, 2})).useAllAvailableWidth();
            table.addHeaderCell(new Cell().add(new Paragraph("Movie Name").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Total Bookings").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Tickets Sold").setBold()));

            for (BookingStatsDto stat : stats) {
                table.addCell(stat.getMovieName());
                table.addCell(String.valueOf(stat.getTotalBookings()));
                table.addCell(String.valueOf(stat.getTicketsSold()));
            }

            document.add(table);
            log.info("Successfully generated Booking Stats PDF report");
        } catch (Exception e) {
            log.error("Error while generating Booking Stats PDF report", e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    @Override
    public ByteArrayInputStream generateRevenueStatsExcelReport() {
        log.info("Generating Revenue Stats Excel report");
        List<RevenueStatsDto> stats = reportRepository.getRevenueStatistics();

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Revenue Statistics");

            String[] headers = {"Movie Name", "Total Revenue"};
            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < headers.length; col++) {
                headerRow.createCell(col).setCellValue(headers[col]);
            }

            int rowIdx = 1;
            for (RevenueStatsDto stat : stats) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(stat.getMovieName());
                row.createCell(1).setCellValue(stat.getTotalRevenue().doubleValue());
            }

            workbook.write(out);
            log.info("Successfully generated Revenue Stats Excel report");
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            log.error("Error while generating Revenue Stats Excel report", e);
            throw new RuntimeException("Failed to generate Excel report: " + e.getMessage());
        }
    }

    @Override
    public ByteArrayInputStream generateRevenueStatsPdfReport() {
        log.info("Generating Revenue Stats PDF report");
        List<RevenueStatsDto> stats = reportRepository.getRevenueStatistics();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try (PdfWriter writer = new PdfWriter(out);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            document.add(new Paragraph("Revenue Statistics Report").setBold().setFontSize(18).setTextAlignment(TextAlignment.CENTER));

            Table table = new Table(UnitValue.createPercentArray(new float[]{3, 2})).useAllAvailableWidth();
            table.addHeaderCell(new Cell().add(new Paragraph("Movie Name").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Total Revenue").setBold()));

            for (RevenueStatsDto stat : stats) {
                table.addCell(stat.getMovieName());
                table.addCell(stat.getTotalRevenue().toPlainString());
            }

            document.add(table);
            log.info("Successfully generated Revenue Stats PDF report");
        } catch (Exception e) {
            log.error("Error while generating Revenue Stats PDF report", e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}