package com.bookmyshow.reportservice.controller;

import com.bookmyshow.reportservice.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private static final Logger log = LoggerFactory.getLogger(ReportController.class);

    @Autowired
    private ReportService reportService;

    @GetMapping("/bookings/excel")
    public ResponseEntity<Resource> downloadBookingStatsExcel() {
        log.info("GET /api/reports/bookings/excel - Request to download booking stats as Excel");
        ByteArrayInputStream in = reportService.generateBookingStatsExcelReport();
        String filename = "booking-stats-" + getFormattedDate() + ".xlsx";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=" + filename);

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(new InputStreamResource(in));
    }

    @GetMapping("/bookings/pdf")
    public ResponseEntity<Resource> downloadBookingStatsPdf() {
        log.info("GET /api/reports/bookings/pdf - Request to download booking stats as PDF");
        ByteArrayInputStream in = reportService.generateBookingStatsPdfReport();
        String filename = "booking-stats-" + getFormattedDate() + ".pdf";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=" + filename);

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(in));
    }

    @GetMapping("/revenue/excel")
    public ResponseEntity<Resource> downloadRevenueStatsExcel() {
        log.info("GET /api/reports/revenue/excel - Request to download revenue stats as Excel");
        ByteArrayInputStream in = reportService.generateRevenueStatsExcelReport();
        String filename = "revenue-stats-" + getFormattedDate() + ".xlsx";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=" + filename);

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(new InputStreamResource(in));
    }

    @GetMapping("/revenue/pdf")
    public ResponseEntity<Resource> downloadRevenueStatsPdf() {
        log.info("GET /api/reports/revenue/pdf - Request to download revenue stats as PDF");
        ByteArrayInputStream in = reportService.generateRevenueStatsPdfReport();
        String filename = "revenue-stats-" + getFormattedDate() + ".pdf";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=" + filename);

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(in));
    }

    private String getFormattedDate() {
        return LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
}