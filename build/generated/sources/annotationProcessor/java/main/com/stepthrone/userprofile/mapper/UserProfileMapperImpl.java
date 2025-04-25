package com.stepthrone.userprofile.mapper;

import com.stepthrone.userprofile.dtos.UserProfileResponse;
import com.stepthrone.userprofile.dtos.UserProfileUpdateRequest;
import com.stepthrone.userprofile.model.User;
import com.stepthrone.userprofile.model.UserProfile;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-24T23:12:40-0500",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.13.jar, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class UserProfileMapperImpl implements UserProfileMapper {

    @Override
    public UserProfileResponse userProfileToUserProfileResponseDto(User user) {
        if ( user == null ) {
            return null;
        }

        String firstName = null;
        String lastName = null;
        String username = null;
        int age = 0;
        double weight = 0.0d;
        double height = 0.0d;

        firstName = userUserProfileFirstName( user );
        lastName = userUserProfileLastName( user );
        username = user.getUsername();
        age = userUserProfileAge( user );
        weight = userUserProfileWeight( user );
        height = userUserProfileHeight( user );

        UserProfileResponse userProfileResponse = new UserProfileResponse( firstName, lastName, username, age, weight, height );

        return userProfileResponse;
    }

    @Override
    public UserProfile userProfileRequestDtoToUserProfile(UserProfileUpdateRequest userProfileRequestDto) {
        if ( userProfileRequestDto == null ) {
            return null;
        }

        UserProfile userProfile = new UserProfile();

        userProfile.setFirstName( userProfileRequestDto.firstName() );
        userProfile.setLastName( userProfileRequestDto.lastName() );
        userProfile.setAge( userProfileRequestDto.age() );
        userProfile.setWeight( userProfileRequestDto.weight() );
        userProfile.setHeight( userProfileRequestDto.height() );

        return userProfile;
    }

    private String userUserProfileFirstName(User user) {
        UserProfile userProfile = user.getUserProfile();
        if ( userProfile == null ) {
            return null;
        }
        return userProfile.getFirstName();
    }

    private String userUserProfileLastName(User user) {
        UserProfile userProfile = user.getUserProfile();
        if ( userProfile == null ) {
            return null;
        }
        return userProfile.getLastName();
    }

    private int userUserProfileAge(User user) {
        UserProfile userProfile = user.getUserProfile();
        if ( userProfile == null ) {
            return 0;
        }
        return userProfile.getAge();
    }

    private double userUserProfileWeight(User user) {
        UserProfile userProfile = user.getUserProfile();
        if ( userProfile == null ) {
            return 0.0d;
        }
        return userProfile.getWeight();
    }

    private double userUserProfileHeight(User user) {
        UserProfile userProfile = user.getUserProfile();
        if ( userProfile == null ) {
            return 0.0d;
        }
        return userProfile.getHeight();
    }
}
