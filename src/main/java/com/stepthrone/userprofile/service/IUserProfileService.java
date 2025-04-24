package com.stepthrone.userprofile.service;

import com.stepthrone.userprofile.dtos.UserProfileResponse;
import com.stepthrone.userprofile.dtos.UserProfileUpdateRequest;

import java.security.Principal;

public interface IUserProfileService {
     UserProfileResponse getProfile(Principal principal );
     UserProfileResponse updateProfile(Principal principal, UserProfileUpdateRequest req) ;
}
