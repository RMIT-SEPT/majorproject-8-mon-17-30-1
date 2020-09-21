package com.rmit.sept.septbackend.service;

import com.rmit.sept.septbackend.entity.AdminEntity;
import com.rmit.sept.septbackend.entity.BusinessEntity;
import com.rmit.sept.septbackend.entity.UserEntity;
import com.rmit.sept.septbackend.model.BusinessResponse;
import com.rmit.sept.septbackend.model.Role;
import com.rmit.sept.septbackend.repository.AdminRepository;
import com.rmit.sept.septbackend.repository.BusinessRepository;
import com.rmit.sept.septbackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@AllArgsConstructor(onConstructor_ = {@Autowired})
@Service
public class BusinessService {

    private final BusinessRepository businessRepository;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    public List<BusinessResponse> getAllBusinesses() {
        return StreamSupport
                .stream(businessRepository.findAll().spliterator(), false)
                .map(businessEntity -> new BusinessResponse(businessEntity.getBusinessName(), businessEntity.getBusinessId()))
                .collect(Collectors.toList());
    }

    public BusinessResponse getBusinessForAdminUsername(String username) {
        UserEntity user = userRepository.getByUsername(username);

        if (user != null) {
            if (user.getRole().equals(Role.ADMIN)) {
                AdminEntity adminEntity = adminRepository.getByUserUsername(username);

                if (adminEntity != null) {
                    BusinessEntity business = adminEntity.getBusiness();

                    return new BusinessResponse(
                            business.getBusinessName(),
                            business.getBusinessId()
                    );

                } else {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, String.format(
                            "Unexpected error occurred when retrieving admin details [username=%s]", username));
                }
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is not an admin!");
            }

        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist");
        }
    }
}
