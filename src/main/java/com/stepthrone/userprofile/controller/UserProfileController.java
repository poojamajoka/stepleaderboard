package com.stepthrone.userprofile.controller;

import com.stepthrone.userprofile.dtos.UserProfileResponse;
import com.stepthrone.userprofile.dtos.UserProfileUpdateRequest;
import com.stepthrone.userprofile.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class UserProfileController {
    private final UserProfileService userService;

    @GetMapping
    public ResponseEntity<UserProfileResponse> getProfile(Principal principal) {
        return ResponseEntity.ok(userService.getProfile(principal));
    }

    @PutMapping
    public ResponseEntity<UserProfileResponse> updateProfile(Principal principal, @RequestBody UserProfileUpdateRequest updateRequest) {
        return ResponseEntity.ok(userService.updateProfile(principal, updateRequest));
    }
}