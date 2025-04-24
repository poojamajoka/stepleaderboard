package com.stepthrone.stepcount.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyStepResponse {
    private Long id;
    private Long userId;
    private LocalDate date;
    private Integer steps;
    private Double calories;
    private Double distance; // in km
    private Instant lastUpdated;
}
