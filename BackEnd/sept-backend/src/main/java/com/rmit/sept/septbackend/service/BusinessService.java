package com.rmit.sept.septbackend.service;

import com.rmit.sept.septbackend.entity.AdminEntity;
import com.rmit.sept.septbackend.entity.BusinessEntity;
import com.rmit.sept.septbackend.entity.UserEntity;
import com.rmit.sept.septbackend.model.BusinessResponse;
import com.rmit.sept.septbackend.model.Role;
import com.rmit.sept.septbackend.model.ValidationResponse;
import com.rmit.sept.septbackend.repository.AdminRepository;
import com.rmit.sept.septbackend.repository.BusinessRepository;
import com.rmit.sept.septbackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public ValidationResponse<BusinessResponse> getBusinessForAdminUsername(String username) {
        ValidationResponse<BusinessResponse> response = new ValidationResponse<>();
        BusinessResponse businessResponse = null;
        UserEntity user = userRepository.getByUsername(username);

        if (user != null) {
            if (user.getRole().equals(Role.ADMIN)) {
                AdminEntity adminEntity = adminRepository.getByUserUsername(username);

                if (adminEntity != null) {
                    BusinessEntity business = adminEntity.getBusiness();

                    businessResponse = new BusinessResponse(
                            business.getBusinessName(),
                            business.getBusinessId()
                    );

                } else {
                    response.addError(
                            "An unexpected error occurred",
                            "Could not retrieve admin details [username=%s]",
                            username
                    );
                }
            } else {
                response.addError(
                        "An unexpected error occurred",
                        "User is not an admin [username=%s,role=%s]",
                        username,
                        user.getRole()
                );
            }

        } else {
            response.addError(
                    "The specified user does not exist",
                    "User does not exist [username=%s]",
                    username
            );

        }

        response.setBody(businessResponse);

        return response;
    }
}
