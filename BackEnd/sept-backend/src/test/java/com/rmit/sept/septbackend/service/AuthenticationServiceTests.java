package com.rmit.sept.septbackend.service;

import com.rmit.sept.septbackend.model.*;
import com.rmit.sept.septbackend.repository.AdminRepository;
import com.rmit.sept.septbackend.repository.CustomerRepository;
import com.rmit.sept.septbackend.repository.UserRepository;
import com.rmit.sept.septbackend.repository.WorkerRepository;
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
import org.springframework.web.server.ResponseStatusException;

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
    private AdminRepository adminRepository;
    @Mock
    private WorkerRepository workerRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    private AuthenticationService authenticationService;

    @BeforeAll
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        authenticationService = new AuthenticationService(authenticationManager, jwtUtils, passwordEncoder, userRepository, adminRepository, workerRepository, customerRepository);
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


        JwtResponse expected = new JwtResponse("token", "test_username", Role.CUSTOMER);

        LoginRequest request = new LoginRequest("test_username", "test_password");
        JwtResponse actual = authenticationService.authenticateUser(request);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testRegisterCustomer() {
        CustomerRegisterRequest customerRegisterRequest = new CustomerRegisterRequest(
                "test_username",
                "test_password",
                "test_first",
                "test_last",
                "test_address",
                "test_city",
                State.QLD,
                "1234"
        );

        Mockito.when(userRepository.existsByUsername(Mockito.any())).thenReturn(false);
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

        JwtResponse actual = authenticationService.registerUser(customerRegisterRequest);

        JwtResponse expected = new JwtResponse("token", "test_username", Role.CUSTOMER);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testExistingCustomer() {
        Mockito.when(userRepository.existsByUsername(Mockito.any())).thenReturn(true);
        Assertions.assertThrows(
                ResponseStatusException.class,
                () -> authenticationService.registerUser(
                        new CustomerRegisterRequest(
                                "test_username",
                                "test_password",
                                "test_first",
                                "test_last",
                                "test_address",
                                "test_city",
                                State.QLD,
                                "1234"
                        )
                )
        );
    }
}