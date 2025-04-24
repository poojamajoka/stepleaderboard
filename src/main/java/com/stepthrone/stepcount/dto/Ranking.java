package com.stepthrone.stepcount.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ranking {
    private Long userId;
    private String firstName;
    private String lastName;
    private Integer totalSteps;
    private Integer rank;

}
