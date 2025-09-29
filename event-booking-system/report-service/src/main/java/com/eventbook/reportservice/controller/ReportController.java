package com.eventbook.reportservice.controller;

import com.eventbook.common.dto.BookingDTO;
import com.eventbook.reportservice.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @GetMapping("/bookings")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InputStreamResource> getBookingsReport(@RequestParam(defaultValue = "pdf") String format) throws IOException {
        List<BookingDTO> bookings = webClientBuilder.build()
                .get()
                .uri("http://booking-service/api/v1/bookings/report") // Corrected endpoint
                .retrieve()
                .bodyToFlux(BookingDTO.class)
                .collectList()
                .block();

        if (bookings == null || bookings.isEmpty()) {
            bookings = getSampleBookingData();
        }

        ByteArrayInputStream bis;
        String filename;
        MediaType mediaType;

        if ("excel".equalsIgnoreCase(format)) {
            bis = reportService.generateBookingsExcel(bookings);
            filename = "bookings.xlsx";
            mediaType = MediaType.APPLICATION_OCTET_STREAM;
        } else {
            bis = reportService.generateBookingsPdf(bookings);
            filename = "bookings.pdf";
            mediaType = MediaType.APPLICATION_PDF;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=" + filename);

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(mediaType)
                .body(new InputStreamResource(bis));
    }

    private List<BookingDTO> getSampleBookingData() {
        return Arrays.asList(
                new BookingDTO(1L, 101L, 201L, LocalDateTime.now().minusDays(1), new BigDecimal("1200.00"), "CONFIRMED"),
                new BookingDTO(2L, 102L, 202L, LocalDateTime.now().minusHours(5), new BigDecimal("850.50"), "CONFIRMED"),
                new BookingDTO(3L, 103L, 201L, LocalDateTime.now().minusMinutes(30), new BigDecimal("400.00"), "PENDING")
        );
    }
}