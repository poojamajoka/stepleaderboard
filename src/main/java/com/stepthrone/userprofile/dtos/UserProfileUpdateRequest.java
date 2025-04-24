package com.stepthrone.userprofile.dtos;

public record UserProfileUpdateRequest(
        String firstName,
        String lastName,
        int age,
        double weight,
        double height
) {}
