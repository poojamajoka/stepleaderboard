package com.stepthrone.auth;


public record RegisterRequest(
        String firstName,
        String lastName,
        String username,
        String password
) {
}
