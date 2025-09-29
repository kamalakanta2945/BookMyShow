package com.eventbook.reportservice.service;

import com.eventbook.reportservice.dto.BookingDTO;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

@Service
public class ReportService {

    /**
     * Generates an Excel report of bookings.
     * @param bookings The list of bookings to include in the report.
     * @return A ByteArrayInputStream containing the Excel file.
     */
    public ByteArrayInputStream generateBookingsExcel(List<BookingDTO> bookings) throws IOException {
        String[] columns = {"ID", "User ID", "Showtime ID", "Booking Time", "Total Price", "Status"};

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Bookings");

            // Header
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            // Data
            int rowIdx = 1;
            for (BookingDTO booking : bookings) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(booking.getId());
                row.createCell(1).setCellValue(booking.getUserId());
                row.createCell(2).setCellValue(booking.getShowtimeId());
                row.createCell(3).setCellValue(booking.getBookingTime().toString());
                row.createCell(4).setCellValue(booking.getTotalPrice().doubleValue());
                row.createCell(5).setCellValue(booking.getStatus());
            }
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    /**
     * Generates a PDF report of bookings.
     * @param bookings The list of bookings to include in the report.
     * @return A ByteArrayInputStream containing the PDF file.
     */
    public ByteArrayInputStream generateBookingsPdf(List<BookingDTO> bookings) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, out);

            document.open();

            Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            font.setSize(18);
            font.setColor(Color.BLUE);

            Paragraph p = new Paragraph("Bookings Report", font);
            p.setAlignment(Paragraph.ALIGN_CENTER);

            document.add(p);

            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100f);
            table.setWidths(new float[]{1.5f, 2.0f, 2.0f, 3.5f, 2.5f, 2.0f});
            table.setSpacingBefore(10);

            // Header
            Stream.of("ID", "User ID", "Showtime ID", "Booking Time", "Total Price", "Status")
                    .forEach(headerTitle -> {
                        PdfPCell header = new PdfPCell();
                        header.setBackgroundColor(Color.LIGHT_GRAY);
                        header.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                        header.setBorderWidth(2);
                        header.setPhrase(new Phrase(headerTitle));
                        table.addCell(header);
                    });

            // Data
            for (BookingDTO booking : bookings) {
                table.addCell(String.valueOf(booking.getId()));
                table.addCell(String.valueOf(booking.getUserId()));
                table.addCell(String.valueOf(booking.getShowtimeId()));
                table.addCell(booking.getBookingTime().toString());
                table.addCell(booking.getTotalPrice().toString());
                table.addCell(booking.getStatus());
            }

            document.add(table);
            document.close();

            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}