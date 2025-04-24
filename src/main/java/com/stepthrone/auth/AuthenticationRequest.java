package com.stepthrone.auth;

public record AuthenticationRequest(
        String username,
        String password
) {
}
