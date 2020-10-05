package com.rmit.sept.septbackend.service;

import com.rmit.sept.septbackend.entity.*;
import com.rmit.sept.septbackend.model.*;
import com.rmit.sept.septbackend.repository.ServiceWorkerAvailabilityRepository;
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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
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
    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private ServiceWorkerAvailabilityRepository serviceWorkerAvailabilityRepository;

    private WorkerService workerService;

    @BeforeAll
    public void setup() {
        MockitoAnnotations.initMocks(this);
        workerService = new WorkerService(serviceWorkerRepository, workerRepository, userRepository, authenticationService, serviceWorkerAvailabilityRepository);
    }

    @Test
    public void testGetWorkersByServiceId() {
        ServiceEntity firstService = new ServiceEntity(0, new BusinessEntity("testBusiness"), "testService", 123, Status.ACTIVE);
        WorkerEntity firstWorker = new WorkerEntity(0, new UserEntity("testUsername", "testPassword", "testFirstName", "testLastName", Role.WORKER), Status.ACTIVE);
        WorkerEntity secondWorker = new WorkerEntity(1, new UserEntity("anotherTestUsername", "anotherTestPassword", "anotherTestFirstName", "anotherTestLastName", Role.WORKER), Status.ACTIVE);
        Mockito.when(serviceWorkerRepository.getAllByServiceServiceIdAndServiceStatusAndWorkerStatus(Mockito.anyInt(), Mockito.any(), Mockito.any()))
                .thenReturn(
                        Arrays.asList(
                                new ServiceWorkerEntity(firstService, firstWorker),
                                new ServiceWorkerEntity(firstService, secondWorker)
                        )
                );

        List<WorkerResponse> expected = Arrays.asList(
                new WorkerResponse(0, "testUsername", "testFirstName", "testLastName"),
                new WorkerResponse(1, "anotherTestUsername", "anotherTestFirstName", "anotherTestLastName")
        );

        List<WorkerResponse> actual = workerService.getWorkersByServiceId(0);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testDeleteWorker() {
        // TODO - implement
    }

    @Test
    public void testViewAvailabilityAllServices() {
        WorkerEntity firstWorker = new WorkerEntity(0, new UserEntity("testUsername", "testPassword", "testFirstName", "testLastName", Role.WORKER), Status.ACTIVE);
        ServiceEntity firstService = new ServiceEntity(0, new BusinessEntity("testBusiness"), "testService", 123, Status.ACTIVE);
        ServiceEntity secondService = new ServiceEntity(0, new BusinessEntity("testBusiness"), "anotherTestService", 456, Status.ACTIVE);

        ServiceWorkerAvailabilityEntity first = new ServiceWorkerAvailabilityEntity(
                0,
                new ServiceWorkerEntity(
                        0,
                        firstService,
                        firstWorker
                ),
                new AvailabilityEntity(
                        0,
                        DayOfWeek.MONDAY,
                        LocalTime.of(9, 0),
                        LocalTime.of(17, 0)
                ),
                LocalDate.of(2020, 10, 5),
                LocalDate.of(2020, 10, 5)
        );

        ServiceWorkerAvailabilityEntity second = new ServiceWorkerAvailabilityEntity(
                0,
                new ServiceWorkerEntity(
                        0,
                        secondService,
                        firstWorker
                ),
                new AvailabilityEntity(
                        0,
                        DayOfWeek.TUESDAY,
                        LocalTime.of(9, 0),
                        LocalTime.of(17, 0)
                ),
                LocalDate.of(2020, 10, 6),
                LocalDate.of(2020, 10, 6)
        );

        Mockito.when(serviceWorkerAvailabilityRepository.getAllByWorkerIdServiceId(Mockito.anyInt(), Mockito.anyInt(), Mockito.any(), Mockito.any()))
                .thenReturn(Arrays.asList(first, second));

        AvailabilityResponse expected = new AvailabilityResponse(
                0,
                LocalDate.of(2020, 10, 2),
                LocalDate.of(2020, 10, 8),
                Arrays.asList(
                        new InnerAvailabilityResponse(
                                "testService",
                                DayOfWeek.MONDAY,
                                LocalTime.of(9, 0),
                                LocalTime.of(17, 0),
                                LocalDate.of(2020, 10, 5),
                                LocalDate.of(2020, 10, 5)
                        ),
                        new InnerAvailabilityResponse(
                                "anotherTestService",
                                DayOfWeek.TUESDAY,
                                LocalTime.of(9, 0),
                                LocalTime.of(17, 0),
                                LocalDate.of(2020, 10, 6),
                                LocalDate.of(2020, 10, 6)
                        )
                )
        );

        AvailabilityResponse actual = workerService.viewAvailability(
                0,
                0,
                LocalDate.of(2020, 10, 5),
                LocalDate.of(2020, 10, 6)
        );

        Assertions.assertEquals(expected, actual);
    }
}
