package com.stepthrone.userprofile.service;

import com.stepthrone.exception.customexception.ProfileNotFoundException;
import com.stepthrone.userprofile.dtos.UserProfileResponse;
import com.stepthrone.userprofile.dtos.UserProfileUpdateRequest;
import com.stepthrone.userprofile.mapper.UserProfileMapper;
import com.stepthrone.userprofile.model.User;
import com.stepthrone.userprofile.model.UserProfile;
import com.stepthrone.userprofile.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class UserProfileService implements IUserProfileService{
    private final UserRepository userRepository;
    private final UserProfileMapper mapper;

    @Override
    public UserProfileResponse getProfile(Principal principal ) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found"));

        return mapper.userProfileToUserProfileResponseDto(user);
    }
    @Override
    public UserProfileResponse updateProfile(Principal principal, UserProfileUpdateRequest req) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found"));

        UserProfile userProfile = mapper.userProfileRequestDtoToUserProfile(req);
        user.setUserProfile(userProfile);
        userRepository.save(user);
        return mapper.userProfileToUserProfileResponseDto(user);
    }
}
