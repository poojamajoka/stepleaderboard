package com.stepthrone.profile.contorller;

import com.stepthrone.userprofile.controller.UserProfileController;
import com.stepthrone.userprofile.dtos.UserProfileResponse;
import com.stepthrone.userprofile.dtos.UserProfileUpdateRequest;
import com.stepthrone.userprofile.service.UserProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserProfileControllerTest {

    @Mock
    private UserProfileService userService;

    @Mock
    private Principal principal;

    @InjectMocks
    private UserProfileController userProfileController;

    private UserProfileResponse mockResponse;
    private UserProfileUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        mockResponse = new UserProfileResponse(
                "John", "Doe", "john.doe", 30, 75.5, 180.0
        );

        updateRequest = new UserProfileUpdateRequest(
                "John", "Doe", 30, 75.5, 180.0
        );
    }

    @Test
    void getProfile_ShouldReturnUserProfile() {
        // Arrange
        when(principal.getName()).thenReturn("john.doe");
        when(userService.getProfile(principal)).thenReturn(mockResponse);

        // Act
        ResponseEntity<UserProfileResponse> response = userProfileController.getProfile(principal);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());
        verify(userService).getProfile(principal);
    }

    @Test
    void updateProfile_ShouldReturnUpdatedProfile() {
        // Arrange
        when(principal.getName()).thenReturn("john.doe");
        when(userService.updateProfile(principal, updateRequest)).thenReturn(mockResponse);

        // Act
        ResponseEntity<UserProfileResponse> response =
                userProfileController.updateProfile(principal, updateRequest);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());
        verify(userService).updateProfile(principal, updateRequest);
    }
}
