package com.bookmyshow.reportservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RevenueStatsDto {
    private String movieName;
    private BigDecimal totalRevenue;
}