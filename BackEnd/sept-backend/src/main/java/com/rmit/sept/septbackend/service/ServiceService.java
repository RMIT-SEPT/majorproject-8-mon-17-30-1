package com.rmit.sept.septbackend.service;

import com.rmit.sept.septbackend.entity.BusinessEntity;
import com.rmit.sept.septbackend.entity.ServiceEntity;
import com.rmit.sept.septbackend.entity.ServiceWorkerAvailabilityEntity;
import com.rmit.sept.septbackend.entity.ServiceWorkerEntity;
import com.rmit.sept.septbackend.model.*;
import com.rmit.sept.septbackend.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServiceService {

    private final ServiceRepository serviceRepository;
    private final BusinessRepository businessRepository;
    private final ServiceWorkerRepository serviceWorkerRepository;
    private final WorkerRepository workerRepository;
    private final ServiceWorkerAvailabilityRepository serviceWorkerAvailabilityRepository;
    private final AvailabilityRepository availabilityRepository;

    public ServiceService(ServiceRepository serviceRepository, BusinessRepository businessRepository, ServiceWorkerRepository serviceWorkerRepository, WorkerRepository workerRepository, ServiceWorkerAvailabilityRepository serviceWorkerAvailabilityRepository, AvailabilityRepository availabilityRepository) {
        this.serviceRepository = serviceRepository;
        this.businessRepository = businessRepository;
        this.serviceWorkerRepository = serviceWorkerRepository;
        this.workerRepository = workerRepository;
        this.serviceWorkerAvailabilityRepository = serviceWorkerAvailabilityRepository;
        this.availabilityRepository = availabilityRepository;
    }

    public void createService(CreateServiceRequest createServiceRequest) {
        Optional<BusinessEntity> businessEntity = businessRepository.findById(createServiceRequest.getBusinessId());
        if (businessEntity.isPresent()) {

            ServiceEntity serviceEntity = new ServiceEntity(
                    businessEntity.get(),
                    createServiceRequest.getServiceName(),
                    createServiceRequest.getDurationMinutes()
            );

            serviceRepository.save(serviceEntity);

        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format(
                            "Provided business name does not exist! [businessId=%s]",
                            createServiceRequest.getBusinessId()
                    )
            );
        }
    }

    public List<ServiceResponse> getServicesForBusinessId(Integer businessId) {
        if (!(businessRepository.existsById(businessId))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Business does not exist");
        }

        List<ServiceEntity> serviceEntities = serviceRepository.getAllByBusinessBusinessIdAndStatus(businessId, Status.ACTIVE);

        return convertServiceEntityToServiceResponse(serviceEntities);
    }

    public List<ServiceResponse> getServicesForUsername(String username) {
        if (!(workerRepository.existsByUserUsername(username))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist");
        }

        List<ServiceWorkerEntity> serviceWorkerEntities = serviceWorkerRepository.getAllByWorkerUserUsernameAndServiceStatusAndWorkerStatus(username, Status.ACTIVE, Status.ACTIVE);
        List<ServiceEntity> serviceEntities = serviceWorkerEntities.stream().map(ServiceWorkerEntity::getService).collect(Collectors.toList());

        return convertServiceEntityToServiceResponse(serviceEntities);
    }

    public void editService(int serviceId, CreateServiceRequest createServiceRequest) {
        Optional<ServiceEntity> optionalServiceEntity = serviceRepository.findById(serviceId);

        if (optionalServiceEntity.isPresent()) {
            ServiceEntity serviceEntity = optionalServiceEntity.get();

            serviceEntity.setServiceName(createServiceRequest.getServiceName());
            serviceEntity.setDurationMinutes(createServiceRequest.getDurationMinutes());

            serviceRepository.save(serviceEntity);

        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Service does not exist");
        }
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
    public void deleteService(int serviceId) {
        Optional<ServiceEntity> entity = serviceRepository.findById(serviceId);
        if (entity.isPresent() && !entity.get().getStatus().equals(Status.CANCELLED)) {
            ServiceEntity serviceEntity = entity.get();

            serviceEntity.setStatus(Status.CANCELLED);

            serviceRepository.save(serviceEntity);

        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Service not found");
        }
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
    public AvailabilityResponse viewServiceAvailability(Integer serviceId, LocalDate effectiveStartDate, LocalDate effectiveEndDate) {
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Service does not exist");
        }

        availabilityEntities = serviceWorkerAvailabilityRepository.getAllByServiceId(
                serviceId,
                effectiveStartDate,
                effectiveEndDate
        );

        return new AvailabilityResponse(
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
        );
    }
}
