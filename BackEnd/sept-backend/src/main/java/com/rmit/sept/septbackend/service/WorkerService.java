package com.rmit.sept.septbackend.service;

import com.rmit.sept.septbackend.entity.ServiceWorkerEntity;
import com.rmit.sept.septbackend.entity.UserEntity;
import com.rmit.sept.septbackend.entity.WorkerEntity;
import com.rmit.sept.septbackend.model.NewWorkerRequest;
import com.rmit.sept.septbackend.model.Role;
import com.rmit.sept.septbackend.model.WorkerResponse;
import com.rmit.sept.septbackend.repository.ServiceWorkerRepository;
import com.rmit.sept.septbackend.repository.UserRepository;
import com.rmit.sept.septbackend.repository.WorkerRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@AllArgsConstructor(onConstructor_ = {@Autowired})
@Service
public class WorkerService {

    private final ServiceWorkerRepository serviceWorkerRepository;
    private final WorkerRepository workerRepository;
    private final UserRepository userRepository;

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

    public List<WorkerResponse> getWorkers() {
        Iterable<WorkerEntity> workers = workerRepository.findAll();
        List<WorkerResponse> workerResponses = new ArrayList<>();
        for (WorkerEntity w : workers) {
            workerResponses.add(new WorkerResponse(w.getWorkerId(), w.getUser().getFirstName(), w.getUser().getLastName()));
        }
        return workerResponses;
    }

    public void createNewWorker(NewWorkerRequest newWorkerRequest) {
        UserEntity newUser = new UserEntity(
                newWorkerRequest.getUsername(),
                newWorkerRequest.getPassword(),
                newWorkerRequest.getFirstName(),
                newWorkerRequest.getLastName(),
                Role.WORKER);

        userRepository.save(newUser);

        WorkerEntity workerEntity = new WorkerEntity(newUser);

        workerRepository.save(workerEntity);
    }
}
