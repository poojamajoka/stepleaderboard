package com.stepthrone.userprofile.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record UserProfileUpdateRequest(
        @NotBlank(message = "First name is required")
        String firstName,

        @NotBlank(message = "Last name is required")
        String lastName,

        @Min(value = 1, message = "Age must be at least 1")
        @Max(value = 120, message = "Age must be less than or equal to 120")
        int age,

        @Positive(message = "Weight must be a positive value")
        double weight,

        @Positive(message = "Height must be a positive value")
        double height
) {}
