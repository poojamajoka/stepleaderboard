package com.stepthrone.stepcount.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyLeaderboardResponse {
    private LocalDate date;
    private List<Ranking> rankings;
}