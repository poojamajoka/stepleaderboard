package com.stepthrone.stepcount.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GlobalLeaderboardResponse {
    private List<Ranking> rankings;
    private int totalPages;
    private long totalElements;
}