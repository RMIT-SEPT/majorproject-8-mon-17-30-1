package com.rmit.sept.septbackend.controller;

import com.rmit.sept.septbackend.model.AvailabilityResponse;
import com.rmit.sept.septbackend.model.CreateServiceRequest;
import com.rmit.sept.septbackend.model.ServiceResponse;
import com.rmit.sept.septbackend.service.ServiceService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.LocalDate;
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
    public List<ServiceResponse> getServicesForBusinessName(@RequestParam(name = "business-id", required = false) Optional<Integer> businessId,
                                                            @RequestParam(name = "username", required = false) Optional<String> username) {
        List<ServiceResponse> serviceResponses;
        if (!(businessId.isPresent() && username.isPresent())) {
            if (businessId.isPresent()) {
                serviceResponses = serviceService.getServicesForBusinessId(businessId.get());

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

    @DeleteMapping("/delete/{serviceId}")
    public void cancelBooking(@Valid @PathVariable int serviceId) {
        serviceService.deleteService(serviceId);
    }

    @PutMapping("/{service-id}")
    public void editService(@PathVariable("service-id") int serviceId, @RequestBody CreateServiceRequest createServiceRequest) {
        serviceService.editService(serviceId, createServiceRequest);
    }

    @GetMapping("/availability/{serviceId}")
    public AvailabilityResponse viewAvailability(@Valid @PathVariable int serviceId,
                                                 @RequestParam(required = false)
                                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate effectiveStartDate,
                                                 @RequestParam(required = false)
                                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate effectiveEndDate) {
        return serviceService.viewServiceAvailability(serviceId, effectiveStartDate, effectiveEndDate);
    }
}
