package com.rmit.sept.septbackend.service;

import com.rmit.sept.septbackend.entity.*;
import com.rmit.sept.septbackend.model.*;
import com.rmit.sept.septbackend.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
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
    private final ServiceWorkerAvailabilityRepository serviceWorkerAvailabilityRepository;
    private final AvailabilityRepository availabilityRepository;

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
     * @param workerId
     * @param serviceId
     * @param effectiveStartDate
     * @param effectiveEndDate
     * @return
     */
    public AvailabilityResponse viewAvailability(int workerId, Integer serviceId, LocalDate effectiveStartDate, LocalDate effectiveEndDate) {
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
        if (serviceId != null) {
            availabilityEntities = serviceWorkerAvailabilityRepository.getAllByWorkerIdServiceId(
                    workerId,
                    serviceId,
                    effectiveStartDate,
                    effectiveEndDate
            );
        } else {
            availabilityEntities = serviceWorkerAvailabilityRepository.getAllByWorkerId(
                    workerId,
                    effectiveStartDate,
                    effectiveEndDate
            );
        }

        return new AvailabilityResponse(
                workerId,
                effectiveStartDate,
                effectiveEndDate,
                availabilityEntities
                        .stream()
                        .map(availabilityEntity ->
                                new InnerAvailabilityResponse(
                                        availabilityEntity.getServiceWorkerAvailabilityId(),
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

    public void addAvailability(AvailabilityRequest availabilityRequest) {
        ServiceWorkerEntity serviceWorkerEntity = serviceWorkerRepository.getByServiceServiceIdAndWorkerWorkerId(
                availabilityRequest.getServiceId(),
                availabilityRequest.getWorkerId()
        );

        if (serviceWorkerEntity == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not find service/worker relationship");
        }

        LocalDate effectiveStartDate = availabilityRequest.getEffectiveStartDate();
        LocalDate effectiveEndDate = availabilityRequest.getEffectiveEndDate();
        if (effectiveStartDate == null) {
            effectiveStartDate = LocalDate.now();
        }
        if (effectiveEndDate == null) {
            effectiveEndDate = effectiveStartDate.plusDays(7);
        }

        AvailabilityEntity availabilityEntity = new AvailabilityEntity(
                availabilityRequest.getDay(),
                availabilityRequest.getStartTime(),
                availabilityRequest.getEndTime()
        );

        ServiceWorkerAvailabilityEntity workerAvailabilityEntity = new ServiceWorkerAvailabilityEntity(
                serviceWorkerEntity,
                availabilityEntity,
                effectiveStartDate,
                effectiveEndDate
        );

        availabilityRepository.save(availabilityEntity);
        serviceWorkerAvailabilityRepository.save(workerAvailabilityEntity);
    }

    public void deleteAvailability(int serviceWorkerAvailabilityId) {
        Optional<ServiceWorkerAvailabilityEntity> entity = serviceWorkerAvailabilityRepository.findById(serviceWorkerAvailabilityId);

        if (entity.isPresent()) {
            serviceWorkerAvailabilityRepository.delete(entity.get());


        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot find availability entry for given id");
        }

    }
}
