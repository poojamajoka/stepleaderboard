package com.stepthrone.stepcount.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyStepRequest {


    @JsonFormat(pattern = "MM-dd-yyyy")
    @NotEmpty(message = "Date is required")
    private LocalDate date;

    @Min(0)
    private Integer steps;
}
