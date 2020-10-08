package com.rmit.sept.septbackend.controller;

import com.rmit.sept.septbackend.model.*;
import com.rmit.sept.septbackend.service.WorkerService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/v1/worker")
@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class WorkerController {

    private final WorkerService workerService;

    @GetMapping()
    public List<WorkerResponse> getWorkers() {
        return workerService.getWorkers();
    }

    @GetMapping("/by/service")
    public List<WorkerResponse> getAllWorkersByServiceId(@RequestParam(value = "service-id") Integer serviceId) {
        return workerService.getWorkersByServiceId(serviceId);
    }

    @PostMapping("/create")
    public void createWorker(@Valid @RequestBody NewWorkerRequest newWorkerRequest) {
        workerService.createNewWorker(newWorkerRequest);
    }

    @PostMapping("/edit")
    public void editWorker(@Valid @RequestBody EditWorkerRequest editWorkerRequest) {
        workerService.editExistingWorker(editWorkerRequest);
    }

    @PostMapping("/availability")
    public void addAvailability(@Valid @RequestBody AvailabilityRequest availabilityRequest) {
        workerService.addAvailability(availabilityRequest);
    }

    @GetMapping("/availability/{workerId}")
    public AvailabilityResponse viewAvailability(@Valid @PathVariable int workerId,
                                                 @RequestParam(required = false) Integer serviceId,
                                                 @RequestParam(required = false)
                                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate effectiveStartDate,
                                                 @RequestParam(required = false)
                                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate effectiveEndDate) {

        return workerService.viewAvailability(workerId, serviceId, effectiveStartDate, effectiveEndDate);
    }

    @DeleteMapping("/availability/{serviceWorkerAvailabilityId}")
    public void deleteAvailability(@Valid @PathVariable int serviceWorkerAvailabilityId) {
        workerService.deleteAvailability(serviceWorkerAvailabilityId);
    }

    @DeleteMapping("/delete/{workerId}")
    public void deleteWorker(@Valid @PathVariable int workerId) {
        workerService.deleteWorker(workerId);
    }

    @PostMapping("/availability/edit/{availabilityId}")
    public void editAvailability(@Valid @PathVariable int availabilityId,
                                 @RequestBody AvailabilityRequest availabilityRequest) {
        workerService.editAvailability(availabilityId, availabilityRequest);
    }
}
