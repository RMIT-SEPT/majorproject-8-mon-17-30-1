package com.rmit.sept.septbackend.controller;

import com.rmit.sept.septbackend.entity.WorkerEntity;
import com.rmit.sept.septbackend.model.BookingRequest;
import com.rmit.sept.septbackend.model.EditWorkerRequest;
import com.rmit.sept.septbackend.model.NewWorkerRequest;
import com.rmit.sept.septbackend.model.WorkerResponse;
import com.rmit.sept.septbackend.service.WorkerService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
        System.out.println("new worker: " + newWorkerRequest.toString());
        workerService.createNewWorker(newWorkerRequest);
    }

    @PostMapping("/edit")
    public void editWorker(@Valid @RequestBody EditWorkerRequest editWorkerRequest) {
        workerService.editExistingWorker(editWorkerRequest);
    }

    @PostMapping("/delete")
    public void deleteWorker(@RequestParam(value = "worker-id") Integer workerId) {
        workerService.deleteWorker(workerId);
    }
}
