package com.stepthrone.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    /***
     * for /register
     */
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        AuthenticationResponse authenticationResponse = authenticationService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationResponse);
    }

    /***
     * for /authenticate
     */
    //AuthenticationResponse authenticate(authenticationRequest)
    @PostMapping("/authenticate")
    private ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        AuthenticationResponse authenticationResponse = authenticationService.authenticate(authenticationRequest);
        return ResponseEntity.status(HttpStatus.OK).body(authenticationResponse);
    }
}
