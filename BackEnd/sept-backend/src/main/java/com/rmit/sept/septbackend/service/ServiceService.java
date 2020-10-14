package com.rmit.sept.septbackend.service;

import com.rmit.sept.septbackend.entity.BusinessEntity;
import com.rmit.sept.septbackend.entity.ServiceEntity;
import com.rmit.sept.septbackend.entity.ServiceWorkerAvailabilityEntity;
import com.rmit.sept.septbackend.entity.ServiceWorkerEntity;
import com.rmit.sept.septbackend.model.*;
import com.rmit.sept.septbackend.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor(onConstructor_ = {@Autowired})
@Service
public class ServiceService {

    private final ServiceRepository serviceRepository;
    private final BusinessRepository businessRepository;
    private final ServiceWorkerRepository serviceWorkerRepository;
    private final WorkerRepository workerRepository;
    private final ServiceWorkerAvailabilityRepository serviceWorkerAvailabilityRepository;
    private final AvailabilityRepository availabilityRepository;

    public ValidationResponse<Void> createService(CreateServiceRequest createServiceRequest) {
        ValidationResponse<Void> response = new ValidationResponse<>();
        Optional<BusinessEntity> businessEntity = businessRepository.findById(createServiceRequest.getBusinessId());
        if (businessEntity.isPresent()) {

            ServiceEntity serviceEntity = new ServiceEntity(
                    businessEntity.get(),
                    createServiceRequest.getServiceName(),
                    createServiceRequest.getDurationMinutes()
            );

            serviceRepository.save(serviceEntity);

        } else {
            response.addError(
                    "An unexpected error occurred - unable to create service",
                    "Business does not exist [businessId=%d]",
                    createServiceRequest.getBusinessId()
            );
        }

        return response;
    }

    public ValidationResponse<List<ServiceResponse>> getServicesByWorkerId(Integer workerId) {
        ValidationResponse<List<ServiceResponse>> response = new ValidationResponse<>();
        if (!(workerRepository.existsById(workerId))) {
            response.addError(
                    "An unexpected error occurred - could not retrieve services",
                    "Worker does not exist [workerId=%d]",
                    workerId
            );
            // Guarding worker
            return response;
        }

        List<ServiceWorkerEntity> serviceEntities = serviceWorkerRepository.getAllByWorkerWorkerId(workerId);
        List<ServiceEntity> services = new ArrayList<>();
        for (ServiceWorkerEntity swe : serviceEntities
        ) {
            if (!services.contains(swe.getService()))
                services.add(swe.getService());
        }

        response.setBody(convertServiceEntityToServiceResponse(services));
        return response;
    }

    public ValidationResponse<List<ServiceResponse>> getServicesForBusinessId(Integer businessId) {
        ValidationResponse<List<ServiceResponse>> response = new ValidationResponse<>();
        if (!(businessRepository.existsById(businessId))) {
            response.addError(
                    "An unexpected error occurred - could not retrieve services",
                    "Business does not exist [businessId=%d]",
                    businessId
            );
            // Guarding business
            return response;
        }

        List<ServiceEntity> serviceEntities = serviceRepository.getAllByBusinessBusinessIdAndStatus(businessId, Status.ACTIVE);

        response.setBody(convertServiceEntityToServiceResponse(serviceEntities));
        return response;
    }

    public ValidationResponse<List<ServiceResponse>> getServicesForUsername(String username) {
        ValidationResponse<List<ServiceResponse>> response = new ValidationResponse<>();
        if (!(workerRepository.existsByUserUsername(username))) {
            response.addError(
                    "An unexpected error occurred - could not retrieve services",
                    "user does not exist [username=%s]",
                    username
            );
            // Guarding business
            return response;
        }

        List<ServiceWorkerEntity> serviceWorkerEntities = serviceWorkerRepository.getAllByWorkerUserUsernameAndServiceStatusAndWorkerStatus(username, Status.ACTIVE, Status.ACTIVE);
        List<ServiceEntity> serviceEntities = serviceWorkerEntities.stream().map(ServiceWorkerEntity::getService).collect(Collectors.toList());

        response.setBody(convertServiceEntityToServiceResponse(serviceEntities));
        return response;
    }

    public ValidationResponse<Void> editService(int serviceId, CreateServiceRequest createServiceRequest) {
        ValidationResponse<Void> response = new ValidationResponse<>();
        Optional<ServiceEntity> optionalServiceEntity = serviceRepository.findById(serviceId);

        if (optionalServiceEntity.isPresent()) {
            ServiceEntity serviceEntity = optionalServiceEntity.get();

            serviceEntity.setServiceName(createServiceRequest.getServiceName());
            serviceEntity.setDurationMinutes(createServiceRequest.getDurationMinutes());

            serviceRepository.save(serviceEntity);

        } else {
            response.addError(
                    "An unexpected error occurred - could not edit service",
                    "Service does not exist [serviceId=%d]",
                    serviceId
            );
        }

        return response;
    }

    private List<ServiceResponse> convertServiceEntityToServiceResponse(List<ServiceEntity> serviceEntities) {
        return serviceEntities
                .stream()
                .map(serviceEntity ->
                        new ServiceResponse(
                                serviceEntity.getServiceId(),
                                serviceEntity.getBusiness().getBusinessName(),
                                serviceEntity.getServiceName(),
                                serviceEntity.getDurationMinutes()
                        )
                )
                .collect(Collectors.toList());
    }

    /**
     * Effectively delete a service given a service-id.
     * Note: this does not delete the row from the database as services are referenced elsewhere (ie. serviceWorker).
     * As such, this only changes the status.
     *
     * @param serviceId
     */
    public ValidationResponse<Void> deleteService(int serviceId) {
        ValidationResponse<Void> response = new ValidationResponse<>();
        Optional<ServiceEntity> entity = serviceRepository.findById(serviceId);
        if (entity.isPresent() && !entity.get().getStatus().equals(Status.CANCELLED)) {
            ServiceEntity serviceEntity = entity.get();

            serviceEntity.setStatus(Status.CANCELLED);

            serviceRepository.save(serviceEntity);

        } else {
            response.addError(
                    "An unexpected error occurred - could not delete service",
                    "Service does not exist [serviceId=%d]",
                    serviceId
            );
        }

        return response;
    }

    /**
     * Works by showing any of the workers availability within the given period.
     * <p>For example, given {@code S} start date and {@code E} end date, the following are applicable availabilities:</p>
     * <pre>
     *        S       E
     * A1   ╞¦═ ═ ═ ═ ═¦═ ╡ | A
     * A2 ╞ ═¦═ ═ ╡    ¦    | A
     * A3    ¦  ╞ ═ ═ ═¦╡   | A
     * A4 ╞ ╡¦         ¦    | N/A
     * A5    ¦         ¦╞ ╡ | N/A
     * A6    ¦  ╞ ═ ╡  ¦    | A
     * </pre>
     *
     * @param serviceId
     * @param effectiveStartDate
     * @param effectiveEndDate
     * @return
     */
    public ValidationResponse<AvailabilityResponse> viewServiceAvailability(Integer serviceId, LocalDate effectiveStartDate, LocalDate effectiveEndDate) {
        ValidationResponse<AvailabilityResponse> response = new ValidationResponse<>();
        // Conveniences when no dates are passed in
        // Start defaults to today, end defaults to 7 days from start
        if (effectiveStartDate == null) {
            effectiveStartDate = LocalDate.now();
        }

        if (effectiveEndDate == null) {
            effectiveEndDate = effectiveStartDate.plusDays(7);
        }

        // The applicable availability magic is done inside the below queries
        List<ServiceWorkerAvailabilityEntity> availabilityEntities;
        if (serviceId == null) {
            response.addError(
                    "An unexpected error occurred - could not retrieve availabilities",
                    "Service does not exist [serviceId=%d]",
                    serviceId
            );
            return response;
        }

        availabilityEntities = serviceWorkerAvailabilityRepository.getAllByServiceId(
                serviceId,
                effectiveStartDate,
                effectiveEndDate
        );

        response.setBody(new AvailabilityResponse(
                effectiveStartDate,
                effectiveEndDate,
                availabilityEntities
                        .stream()
                        .map(availabilityEntity ->
                                new InnerAvailabilityResponse(
                                        availabilityEntity.getServiceWorkerAvailabilityId(),
                                        availabilityEntity.getServiceWorker().getWorker().getWorkerId(),
                                        availabilityEntity.getServiceWorker().getService().getServiceName(),
                                        availabilityEntity.getAvailability().getDay(),
                                        availabilityEntity.getAvailability().getStartTime(),
                                        availabilityEntity.getAvailability().getEndTime(),
                                        availabilityEntity.getEffectiveStartDate(),
                                        availabilityEntity.getEffectiveEndDate()
                                )
                        )
                        .collect(Collectors.toList())
        ));
        return response;
    }
}
