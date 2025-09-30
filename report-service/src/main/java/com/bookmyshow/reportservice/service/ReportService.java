package com.bookmyshow.reportservice.service;

import java.io.ByteArrayInputStream;

public interface ReportService {

    ByteArrayInputStream generateBookingStatsExcelReport();

    ByteArrayInputStream generateBookingStatsPdfReport();

    ByteArrayInputStream generateRevenueStatsExcelReport();

    ByteArrayInputStream generateRevenueStatsPdfReport();
}