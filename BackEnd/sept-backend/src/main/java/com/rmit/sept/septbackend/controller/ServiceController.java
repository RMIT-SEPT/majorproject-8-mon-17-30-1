package com.rmit.sept.septbackend.controller;

import com.rmit.sept.septbackend.entity.ServiceEntity;
import com.rmit.sept.septbackend.model.CreateServiceRequest;
import com.rmit.sept.septbackend.model.ServiceResponse;
import com.rmit.sept.septbackend.service.ServiceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/service")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ServiceController {

    private final ServiceService serviceService;

    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @PostMapping
    public void createService(@Valid @RequestBody CreateServiceRequest createServiceRequest) {
        serviceService.createService(createServiceRequest);
    }

    @GetMapping
    public List<ServiceResponse> getServicesForBusinessName(@RequestParam(name = "business-name", required = false) Optional<String> businessName,
                                                            @RequestParam(name = "username", required = false) Optional<String> username) {
        List<ServiceResponse> serviceResponses;
        if (!(businessName.isPresent() && username.isPresent())) {
            if (businessName.isPresent()) {
                serviceResponses = serviceService.getServicesForBusinessName(businessName.get());

            } else if (username.isPresent()) {
                serviceResponses = serviceService.getServicesForUsername(username.get());

            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No parameters specified");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only specify one parameter");
        }
        return serviceResponses;
    }

}
