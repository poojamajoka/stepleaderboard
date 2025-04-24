package com.stepthrone.userprofile.mapper;

import com.stepthrone.userprofile.dtos.UserProfileResponse;
import com.stepthrone.userprofile.dtos.UserProfileUpdateRequest;
import com.stepthrone.userprofile.model.User;
import com.stepthrone.userprofile.model.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    @Mapping(source = "userProfile.firstName", target = "firstName")
    @Mapping(source = "userProfile.lastName", target = "lastName")
    @Mapping(source = "username", target = "username") // From User itself
    @Mapping(source = "userProfile.age", target = "age")
    @Mapping(source = "userProfile.weight", target = "weight")
    @Mapping(source = "userProfile.height", target = "height")
    UserProfileResponse userProfileToUserProfileResponseDto(User user);

    UserProfile userProfileRequestDtoToUserProfile(UserProfileUpdateRequest userProfileRequestDto);
}
