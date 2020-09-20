package com.rmit.sept.septbackend.service;

import com.rmit.sept.septbackend.entity.BusinessEntity;
import com.rmit.sept.septbackend.entity.ServiceEntity;
import com.rmit.sept.septbackend.entity.ServiceWorkerEntity;
import com.rmit.sept.septbackend.model.CreateServiceRequest;
import com.rmit.sept.septbackend.model.ServiceResponse;
import com.rmit.sept.septbackend.repository.BusinessRepository;
import com.rmit.sept.septbackend.repository.ServiceRepository;
import com.rmit.sept.septbackend.repository.ServiceWorkerRepository;
import com.rmit.sept.septbackend.repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceService {

    private final ServiceRepository serviceRepository;
    private final BusinessRepository businessRepository;
    private final ServiceWorkerRepository serviceWorkerRepository;
    private final WorkerRepository workerRepository;

    public ServiceService(ServiceRepository serviceRepository, BusinessRepository businessRepository, ServiceWorkerRepository serviceWorkerRepository, WorkerRepository workerRepository) {
        this.serviceRepository = serviceRepository;
        this.businessRepository = businessRepository;
        this.serviceWorkerRepository = serviceWorkerRepository;
        this.workerRepository = workerRepository;
    }

    public void createService(CreateServiceRequest createServiceRequest) {
        BusinessEntity businessEntity = businessRepository.getByBusinessName(createServiceRequest.getBusinessName());
        if (businessEntity != null) {

            ServiceEntity serviceEntity = new ServiceEntity(
                    businessEntity,
                    createServiceRequest.getServiceName(),
                    createServiceRequest.getDurationMinutes()
            );

            serviceRepository.save(serviceEntity);

        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format(
                            "Provided business name does not exist! [businessName=%s]",
                            createServiceRequest.getBusinessName()
                    )
            );
        }
    }

    public List<ServiceResponse> getServicesForBusinessId(Integer businessId) {
        if (!(businessRepository.existsById(businessId))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Business does not exist");
        }

        List<ServiceEntity> serviceEntities = serviceRepository.getAllByBusinessBusinessId(businessId);

        return convertServiceEntityToServiceResponse(serviceEntities);
    }

    public List<ServiceResponse> getServicesForUsername(String username) {
        if (!(workerRepository.existsByUserUsername(username))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist");
        }

        List<ServiceWorkerEntity> serviceWorkerEntities = serviceWorkerRepository.getAllByWorkerUserUsername(username);
        List<ServiceEntity> serviceEntities = serviceWorkerEntities.stream().map(ServiceWorkerEntity::getService).collect(Collectors.toList());

        return convertServiceEntityToServiceResponse(serviceEntities);
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
}
