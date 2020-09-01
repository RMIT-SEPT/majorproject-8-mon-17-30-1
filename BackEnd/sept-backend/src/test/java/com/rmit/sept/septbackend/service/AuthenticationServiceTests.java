package com.rmit.sept.septbackend.service;

import com.rmit.sept.septbackend.model.JwtResponse;
import com.rmit.sept.septbackend.model.LoginRequest;
import com.rmit.sept.septbackend.model.Response;
import com.rmit.sept.septbackend.model.Role;
import com.rmit.sept.septbackend.repository.UserRepository;
import com.rmit.sept.septbackend.security.JwtUtils;
import com.rmit.sept.septbackend.security.UserDetailsImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthenticationServiceTests {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    private AuthenticationService authenticationService;

    @BeforeAll
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        authenticationService = new AuthenticationService(authenticationManager, jwtUtils, userRepository, passwordEncoder);
    }

    @Test
    public void testCreatingJwtResponse() {
        List<SimpleGrantedAuthority> customer = Collections.singletonList(new SimpleGrantedAuthority("CUSTOMER"));
        Mockito.when(authenticationManager.authenticate(Mockito.any()))
                .thenReturn(new UsernamePasswordAuthenticationToken(
                                new UserDetailsImpl(
                                        001,
                                        "test_username",
                                        "test_password",
                                        customer
                                ),
                                null,
                                customer
                        )
                );
        Mockito.when(jwtUtils.generateJwtToken(Mockito.any()))
                .thenReturn("token");


        Response expected = new JwtResponse("token", "test_username", Role.CUSTOMER);

        LoginRequest request = new LoginRequest("test_username", "test_password");
        Response actual = authenticationService.authenticateUser(request);

        Assertions.assertEquals(expected, actual);
    }
}
