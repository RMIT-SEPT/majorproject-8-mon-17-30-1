package com.rmit.sept.septbackend.service;

import com.rmit.sept.septbackend.entity.*;
import com.rmit.sept.septbackend.model.*;
import com.rmit.sept.septbackend.repository.AdminRepository;
import com.rmit.sept.septbackend.repository.CustomerRepository;
import com.rmit.sept.septbackend.repository.UserRepository;
import com.rmit.sept.septbackend.repository.WorkerRepository;
import com.rmit.sept.septbackend.security.JwtUtils;
import com.rmit.sept.septbackend.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final WorkerRepository workerRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder encoder;

    @Autowired
    public AuthenticationService(AuthenticationManager authenticationManager, JwtUtils jwtUtils, PasswordEncoder encoder, UserRepository userRepository, AdminRepository adminRepository, WorkerRepository workerRepository, CustomerRepository customerRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.encoder = encoder;
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.workerRepository = workerRepository;
        this.customerRepository = customerRepository;
    }

    public JwtResponse authenticateUser(LoginRequest loginRequest) {

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

    public JwtResponse registerUser(RegisterRequest registerRequest) {
        JwtResponse response;

        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is already taken");
        } else {

            // Create new user's account
            UserEntity user = new UserEntity(
                    registerRequest.getUsername(),
                    encoder.encode(registerRequest.getPassword()),
                    registerRequest.getFirstName(),
                    registerRequest.getLastName(),
                    registerRequest.getRole()
            );

            userRepository.save(user);

            switch (registerRequest.getRole()) {
                case ADMIN:
                    AdminRegisterRequest adminRegisterRequest;
                    try {
                        adminRegisterRequest = (AdminRegisterRequest) registerRequest;
                    } catch (ClassCastException e) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mismatched role and for given request");
                    }

                    AdminEntity adminEntity = new AdminEntity();
                    adminEntity.setUser(user);
                    adminEntity.setBusiness(new BusinessEntity(adminRegisterRequest.getBusinessName()));

                    adminRepository.save(adminEntity);
                    break;

                case WORKER:
                    WorkerRegisterRequest workerRegisterRequest;
                    try {
                        workerRegisterRequest = (WorkerRegisterRequest) registerRequest;
                    } catch (ClassCastException e) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mismatched role and for given request");
                    }

                    WorkerEntity workerEntity = new WorkerEntity(user);

                    workerRepository.save(workerEntity);
                    break;

                case CUSTOMER:
                    CustomerRegisterRequest customerRegisterRequest;
                    try {
                        customerRegisterRequest = (CustomerRegisterRequest) registerRequest;
                    } catch (ClassCastException e) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mismatched role and for given request");
                    }

                    CustomerEntity customerEntity = new CustomerEntity(
                            user,
                            customerRegisterRequest.getStreetAddress(),
                            customerRegisterRequest.getCity(),
                            customerRegisterRequest.getState(),
                            customerRegisterRequest.getPostcode()
                    );

                    customerRepository.save(customerEntity);
                    break;
            }

            response = authenticateUser(new LoginRequest(registerRequest.getUsername(), registerRequest.getPassword()));
        }

        return response;
    }
}
