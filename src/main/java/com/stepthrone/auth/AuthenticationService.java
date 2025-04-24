package com.stepthrone.auth;


import com.stepthrone.config.JwtService;
import com.stepthrone.userprofile.model.User;
import com.stepthrone.userprofile.model.UserProfile;
import com.stepthrone.userprofile.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.username(),
                        authenticationRequest.password()
                )
        );
        //generate token for the user
        var user = (User) authentication.getPrincipal();
        String token = jwtService.generateToken(user);
        return new AuthenticationResponse(token);
    }

    public AuthenticationResponse register(RegisterRequest registerRequest) {
        UserProfile userProfile = new UserProfile(registerRequest.firstName(),
                registerRequest.lastName());

        User user = new User(
                registerRequest.username(),
                passwordEncoder.encode(registerRequest.password())
        );
        user.setUserProfile(userProfile);
        User registeredUser = userRepository.save(user);
        //Generate token for this user
        String token = jwtService.generateToken(registeredUser);
        return new AuthenticationResponse(token);
    }
}
