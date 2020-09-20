package com.rmit.sept.septbackend.service;

import com.rmit.sept.septbackend.entity.*;
import com.rmit.sept.septbackend.model.Role;
import com.rmit.sept.septbackend.model.WorkerResponse;
import com.rmit.sept.septbackend.repository.ServiceWorkerRepository;
import com.rmit.sept.septbackend.repository.UserRepository;
import com.rmit.sept.septbackend.repository.WorkerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WorkerServiceTests {

    @Mock
    private ServiceWorkerRepository serviceWorkerRepository;
    @Mock
    private WorkerRepository workerRepository;
    @Mock
    private UserRepository userRepository;

    private WorkerService workerService;

    @BeforeAll
    public void setup() {
        MockitoAnnotations.initMocks(this);
        workerService = new WorkerService(serviceWorkerRepository, workerRepository, userRepository);
    }

    @Test
    public void testGetWorkersByServiceId() {
        ServiceEntity firstService = new ServiceEntity(0, new BusinessEntity("testBusiness"), "testService", 123);
        WorkerEntity firstWorker = new WorkerEntity(0, new UserEntity("testUsername", "testPassword", "testFirstName", "testLastName", Role.WORKER));
        WorkerEntity secondWorker = new WorkerEntity(1, new UserEntity("anotherTestUsername", "anotherTestPassword", "anotherTestFirstName", "anotherTestLastName", Role.WORKER));
        Mockito.when(serviceWorkerRepository.getAllByServiceServiceId(Mockito.anyInt()))
                .thenReturn(
                        Arrays.asList(
                                new ServiceWorkerEntity(firstService, firstWorker),
                                new ServiceWorkerEntity(firstService, secondWorker)
                        )
                );

        List<WorkerResponse> expected = Arrays.asList(
                new WorkerResponse(0, "testFirstName", "testLastName"),
                new WorkerResponse(1, "anotherTestFirstName", "anotherTestLastName")
        );

        List<WorkerResponse> actual = workerService.getWorkersByServiceId(0);

        Assertions.assertEquals(expected, actual);
    }
}
