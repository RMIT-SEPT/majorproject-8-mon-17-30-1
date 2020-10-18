package com.rmit.sept.septbackend.controller;

import com.rmit.sept.septbackend.model.JwtResponse;
import com.rmit.sept.septbackend.model.LoginRequest;
import com.rmit.sept.septbackend.model.RegisterRequest;
import com.rmit.sept.septbackend.service.AuthenticationService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthenticationController extends AbstractBaseController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public JwtResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return authenticationService.authenticateUser(loginRequest);
    }

    @PostMapping("/register")
    public JwtResponse registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        return handleValidationResponse(authenticationService.registerUser(registerRequest));
    }
}
