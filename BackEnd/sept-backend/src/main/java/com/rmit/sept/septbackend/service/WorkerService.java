package com.rmit.sept.septbackend.service;

import com.rmit.sept.septbackend.entity.ServiceWorkerEntity;
import com.rmit.sept.septbackend.model.WorkerResponse;
import com.rmit.sept.septbackend.repository.ServiceWorkerRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor(onConstructor_ = {@Autowired})
@Service
public class WorkerService {

    private final ServiceWorkerRepository serviceWorkerRepository;

    public List<WorkerResponse> getWorkersByServiceId(int serviceId) {
        List<ServiceWorkerEntity> serviceWorkers = serviceWorkerRepository.getAllByServiceServiceId(serviceId);

        return serviceWorkers.stream().map(serviceWorkerEntity ->
                new WorkerResponse(
                        serviceWorkerEntity.getWorker().getWorkerId(),
                        serviceWorkerEntity.getWorker().getUser().getFirstName(),
                        serviceWorkerEntity.getWorker().getUser().getLastName()
                )
        ).collect(Collectors.toList());
    }
}
