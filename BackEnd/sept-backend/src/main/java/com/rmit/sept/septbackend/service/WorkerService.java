package com.rmit.sept.septbackend.service;

import com.rmit.sept.septbackend.entity.ServiceEntity;
import com.rmit.sept.septbackend.entity.ServiceWorkerEntity;
import com.rmit.sept.septbackend.entity.UserEntity;
import com.rmit.sept.septbackend.entity.WorkerEntity;
import com.rmit.sept.septbackend.model.*;
import com.rmit.sept.septbackend.repository.ServiceWorkerRepository;
import com.rmit.sept.septbackend.repository.UserRepository;
import com.rmit.sept.septbackend.repository.WorkerRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor(onConstructor_ = {@Autowired})
@Service
public class WorkerService {

    private final ServiceWorkerRepository serviceWorkerRepository;
    private final WorkerRepository workerRepository;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;

    public List<WorkerResponse> getWorkersByServiceId(int serviceId) {
        List<ServiceWorkerEntity> serviceWorkers = serviceWorkerRepository.getAllByServiceServiceIdAndServiceStatusAndWorkerStatus(serviceId, Status.ACTIVE, Status.ACTIVE);

        return serviceWorkers.stream().map(serviceWorkerEntity ->
                new WorkerResponse(
                        serviceWorkerEntity.getWorker().getWorkerId(),
                        serviceWorkerEntity.getWorker().getUser().getUsername(),
                        serviceWorkerEntity.getWorker().getUser().getFirstName(),
                        serviceWorkerEntity.getWorker().getUser().getLastName()
                )
        ).collect(Collectors.toList());
    }

    public List<WorkerResponse> getWorkers() {
        Iterable<WorkerEntity> workers = workerRepository.findAllByStatus(Status.ACTIVE);
        List<WorkerResponse> workerResponses = new ArrayList<>();
        for (WorkerEntity w : workers) {
            workerResponses.add(new WorkerResponse(w.getWorkerId(), w.getUser().getUsername(), w.getUser().getFirstName(), w.getUser().getLastName()));
        }
        return workerResponses;
    }

    public void createNewWorker(NewWorkerRequest newWorkerRequest) {
        UserEntity newUser = authenticationService.createUser(
                new RegisterRequest(
                        newWorkerRequest.getUsername(),
                        "temppw",
                        newWorkerRequest.getFirstName(),
                        newWorkerRequest.getLastName(),
                        new WorkerRegisterArguments()
                )
        );
    }

    public void editExistingWorker(EditWorkerRequest editWorkerRequest) {
        WorkerEntity worker = workerRepository.getByWorkerId(editWorkerRequest.getWorkerId());
        worker.getUser().setFirstName(editWorkerRequest.getFirstName());
        worker.getUser().setLastName(editWorkerRequest.getLastName());
        worker.getUser().setUsername(editWorkerRequest.getUsername());
        workerRepository.save(worker);
    }

    public void deleteWorker(int workerId) {
        Optional<WorkerEntity> optionalWorkerEntity = workerRepository.findById(workerId);

        if (optionalWorkerEntity.isPresent() && !optionalWorkerEntity.get().getStatus().equals(Status.CANCELLED)) {
            WorkerEntity workerEntity = optionalWorkerEntity.get();

            UserEntity userEntity = workerEntity.getUser();

            // Keep the worker but delete the user
            workerEntity.setUser(null);
            workerEntity.setStatus(Status.CANCELLED);

            workerRepository.save(workerEntity);
            userRepository.delete(userEntity);

        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Worker does not exist");
        }
    }
}
