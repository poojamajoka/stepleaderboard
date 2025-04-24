package com.stepthrone.userprofile.dtos;

public record UserProfileResponse(
        String firstName,
        String lastName,
        String username,
        int age,
        double weight,
        double height
) {}
