package com.bookmyshow.reportservice.repository;

import com.bookmyshow.reportservice.dto.BookingStatsDto;
import com.bookmyshow.reportservice.dto.RevenueStatsDto;

import java.util.List;

public interface ReportRepository {

    List<BookingStatsDto> getBookingStatistics();

    List<RevenueStatsDto> getRevenueStatistics();
}