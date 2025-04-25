package com.stepthrone.profile.service;

import com.stepthrone.exception.customexception.ProfileNotFoundException;
import com.stepthrone.userprofile.dtos.UserProfileResponse;
import com.stepthrone.userprofile.dtos.UserProfileUpdateRequest;
import com.stepthrone.userprofile.mapper.UserProfileMapper;
import com.stepthrone.userprofile.model.User;
import com.stepthrone.userprofile.model.UserProfile;
import com.stepthrone.userprofile.repository.UserRepository;
import com.stepthrone.userprofile.service.UserProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserProfileServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserProfileMapper mapper;

    @Mock
    private Principal principal;

    @InjectMocks
    private UserProfileService userProfileService;

    private User testUser;
    private UserProfileUpdateRequest updateRequest;
    private UserProfileResponse mockResponse;

    @BeforeEach
    void setUp() {
        UserProfile profile = new UserProfile();
        profile.setFirstName("John");
        profile.setLastName("Doe");
        profile.setAge(30);
        profile.setWeight(75.5);
        profile.setHeight(180.0);

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("john.doe");
        testUser.setUserProfile(profile);

        updateRequest = new UserProfileUpdateRequest(
                "John", "Doe", 30, 75.5, 180.0
        );

        mockResponse = new UserProfileResponse(
                "John", "Doe", "john.doe", 30, 75.5, 180.0
        );
    }

    @Test
    void getProfile_ShouldReturnProfileWhenUserExists() {
        // Arrange
        when(principal.getName()).thenReturn("john.doe");
        when(userRepository.findByUsername("john.doe")).thenReturn(Optional.of(testUser));
        when(mapper.userProfileToUserProfileResponseDto(testUser)).thenReturn(mockResponse);

        // Act
        UserProfileResponse response = userProfileService.getProfile(principal);

        // Assert
        assertNotNull(response);
        assertEquals("John", response.firstName());
        assertEquals("Doe", response.lastName());
        verify(userRepository).findByUsername("john.doe");
    }

    @Test
    void getProfile_ShouldThrowExceptionWhenUserNotFound() {
        // Arrange
        when(principal.getName()).thenReturn("unknown.user");
        when(userRepository.findByUsername("unknown.user")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProfileNotFoundException.class, () ->
                userProfileService.getProfile(principal));
        verify(userRepository).findByUsername("unknown.user");
    }

    @Test
    void updateProfile_ShouldUpdateAndReturnProfile() {
        // Arrange
        UserProfile updatedProfile = new UserProfile();
        updatedProfile.setFirstName("John");
        updatedProfile.setLastName("Doe");
        updatedProfile.setAge(31);
        updatedProfile.setWeight(76.0);
        updatedProfile.setHeight(180.5);

        UserProfileResponse updatedResponse = new UserProfileResponse(
                "John", "Doe", "john.doe", 31, 76.0, 180.5
        );

        when(principal.getName()).thenReturn("john.doe");
        when(userRepository.findByUsername("john.doe")).thenReturn(Optional.of(testUser));
        when(mapper.userProfileRequestDtoToUserProfile(updateRequest)).thenReturn(updatedProfile);
        when(userRepository.save(testUser)).thenReturn(testUser);
        when(mapper.userProfileToUserProfileResponseDto(testUser)).thenReturn(updatedResponse);

        // Act
        UserProfileResponse response = userProfileService.updateProfile(principal, updateRequest);

        // Assert
        assertNotNull(response);
        assertEquals(31, response.age());
        assertEquals(76.0, response.weight());
        assertEquals(180.5, response.height());
        verify(userRepository).save(testUser);
    }

    @Test
    void updateProfile_ShouldThrowExceptionWhenUserNotFound() {
        // Arrange
        when(principal.getName()).thenReturn("unknown.user");
        when(userRepository.findByUsername("unknown.user")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProfileNotFoundException.class, () ->
                userProfileService.updateProfile(principal, updateRequest));
        verify(userRepository).findByUsername("unknown.user");
    }
}