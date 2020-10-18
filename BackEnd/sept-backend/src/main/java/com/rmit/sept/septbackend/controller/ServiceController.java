package com.rmit.sept.septbackend.controller;

import com.rmit.sept.septbackend.exception.Error;
import com.rmit.sept.septbackend.exception.ResponseException;
import com.rmit.sept.septbackend.model.AvailabilityResponse;
import com.rmit.sept.septbackend.model.CreateServiceRequest;
import com.rmit.sept.septbackend.model.ServiceResponse;
import com.rmit.sept.septbackend.service.ServiceService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/service")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ServiceController extends AbstractBaseController {

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
                serviceResponses = handleValidationResponse(serviceService.getServicesForBusinessId(businessId.get()));

            } else if (username.isPresent()) {
                serviceResponses = handleValidationResponse(serviceService.getServicesForUsername(username.get()));

            } else {
                throw new ResponseException(Collections.singletonList(new Error("An unexpected error occurred", "No parameters specified")));
            }
        } else {
            throw new ResponseException(Collections.singletonList(new Error("An unexpected error occurred", "Must only specify one parameter")));
        }
        return serviceResponses;
    }

    @GetMapping("/service")
    public List<ServiceResponse> getServicesByWorker(@Valid @RequestParam(name = "worker-id") int workerId) {
        return handleValidationResponse(serviceService.getServicesByWorkerId(workerId));
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
        return handleValidationResponse(serviceService.viewServiceAvailability(serviceId, effectiveStartDate, effectiveEndDate));
    }
}
