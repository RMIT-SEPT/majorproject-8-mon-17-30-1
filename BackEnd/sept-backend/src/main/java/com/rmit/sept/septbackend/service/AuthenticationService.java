package com.rmit.sept.septbackend.service;

import com.rmit.sept.septbackend.entity.UserEntity;
import com.rmit.sept.septbackend.model.*;
import com.rmit.sept.septbackend.repository.UserRepository;
import com.rmit.sept.septbackend.security.JwtUtils;
import com.rmit.sept.septbackend.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Autowired
    public AuthenticationService(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserRepository userRepository, PasswordEncoder encoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public Response authenticateUser(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Role role = Role.valueOf(userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()).get(0));

        return new JwtResponse(
                jwt,
                userDetails.getUsername(),
                role
        );
    }

    public Response registerUser(RegisterRequest registerRequest) {

        Response response;
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            response = new ErrorResponse("Error: Username is already taken!");
        } else {

            // Create new user's account
            UserEntity user = new UserEntity(
                    registerRequest.getUsername(),
                    encoder.encode(registerRequest.getPassword()),
                    registerRequest.getFirstName(),
                    registerRequest.getLastName(),
                    Role.CUSTOMER
            );

            userRepository.save(user);

            response = authenticateUser(new LoginRequest(registerRequest.getUsername(), registerRequest.getPassword()));
        }

        return response;
    }
}
