package com.rmit.sept.septbackend.controller;

import com.rmit.sept.septbackend.model.WorkerResponse;
import com.rmit.sept.septbackend.service.WorkerService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/worker")
@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class WorkerController {

    private final WorkerService workerService;

    @GetMapping
    public List<WorkerResponse> getAllWorkersByServiceId(@RequestParam(value = "service-id") Integer serviceId) {
        return workerService.getWorkersByServiceId(serviceId);
    }
}
