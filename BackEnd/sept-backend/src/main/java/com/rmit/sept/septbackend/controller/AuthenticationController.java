package com.rmit.sept.septbackend.controller;

import com.rmit.sept.septbackend.model.JwtResponse;
import com.rmit.sept.septbackend.model.LoginRequest;
import com.rmit.sept.septbackend.model.RegisterRequest;
import com.rmit.sept.septbackend.model.Response;
import com.rmit.sept.septbackend.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<Response> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Response response = authenticationService.authenticateUser(loginRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<Response> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        Response response = authenticationService.registerUser(registerRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
