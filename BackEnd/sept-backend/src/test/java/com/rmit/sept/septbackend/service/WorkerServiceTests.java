package com.rmit.sept.septbackend.service;

import com.rmit.sept.septbackend.entity.*;
import com.rmit.sept.septbackend.model.*;
import com.rmit.sept.septbackend.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
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
    @Mock
    private AvailabilityRepository availabilityRepository;

    private WorkerService workerService;

    @BeforeAll
    public void setup() {
        MockitoAnnotations.initMocks(this);
        workerService = new WorkerService(serviceWorkerRepository, workerRepository, userRepository, authenticationService, serviceWorkerAvailabilityRepository, availabilityRepository);
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
                1,
                new ServiceWorkerEntity(
                        0,
                        secondService,
                        firstWorker
                ),
                new AvailabilityEntity(
                        1,
                        DayOfWeek.TUESDAY,
                        LocalTime.of(9, 0),
                        LocalTime.of(17, 0)
                ),
                LocalDate.of(2020, 10, 6),
                LocalDate.of(2020, 10, 6)
        );

        Mockito.when(serviceWorkerAvailabilityRepository.getAllByWorkerId(Mockito.anyInt(), Mockito.any(), Mockito.any()))
                .thenReturn(Arrays.asList(first, second));

        AvailabilityResponse expected = new AvailabilityResponse(
                0,
                LocalDate.of(2020, 10, 5),
                LocalDate.of(2020, 10, 6),
                Arrays.asList(
                        new InnerAvailabilityResponse(
                                0,
                                "testService",
                                DayOfWeek.MONDAY,
                                LocalTime.of(9, 0),
                                LocalTime.of(17, 0),
                                LocalDate.of(2020, 10, 5),
                                LocalDate.of(2020, 10, 5)
                        ),
                        new InnerAvailabilityResponse(
                                1,
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
                null,
                LocalDate.of(2020, 10, 5),
                LocalDate.of(2020, 10, 6)
        );

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testViewAvailabilitySingleService() {
        WorkerEntity firstWorker = new WorkerEntity(0, new UserEntity("testUsername", "testPassword", "testFirstName", "testLastName", Role.WORKER), Status.ACTIVE);
        ServiceEntity firstService = new ServiceEntity(0, new BusinessEntity("testBusiness"), "testService", 123, Status.ACTIVE);

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

        Mockito.when(serviceWorkerAvailabilityRepository.getAllByWorkerIdServiceId(Mockito.anyInt(), Mockito.anyInt(), Mockito.any(), Mockito.any()))
                .thenReturn(Collections.singletonList(first));

        AvailabilityResponse expected = new AvailabilityResponse(
                0,
                LocalDate.of(2020, 10, 5),
                LocalDate.of(2020, 10, 6),
                Collections.singletonList(
                        new InnerAvailabilityResponse(
                                0,
                                "testService",
                                DayOfWeek.MONDAY,
                                LocalTime.of(9, 0),
                                LocalTime.of(17, 0),
                                LocalDate.of(2020, 10, 5),
                                LocalDate.of(2020, 10, 5)
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

    @Test
    public void testAddAvailability() {
        ServiceWorkerEntity testServiceWorkerEntity = new ServiceWorkerEntity(
                new ServiceEntity(
                        new BusinessEntity("testBusiness"),
                        "testService",
                        30),
                new WorkerEntity(
                        new UserEntity(
                                "testUsername",
                                "testHashedPassword",
                                "testFirst",
                                "testLast",
                                Role.WORKER
                        )
                )
        );

        Mockito.when(serviceWorkerRepository.getByServiceServiceIdAndWorkerWorkerId(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(testServiceWorkerEntity);

        AvailabilityRequest availabilityRequest = new AvailabilityRequest(
                0,
                0,
                DayOfWeek.MONDAY,
                LocalTime.of(9, 0),
                LocalTime.of(17, 0),
                LocalDate.of(2020, 10, 5),
                LocalDate.of(2020, 10, 5)
        );

        AvailabilityEntity expectedAvailable = new AvailabilityEntity(
                DayOfWeek.MONDAY,
                LocalTime.of(9, 0),
                LocalTime.of(17, 0)
        );
        ServiceWorkerAvailabilityEntity expectedWorkerAvailable = new ServiceWorkerAvailabilityEntity(
                testServiceWorkerEntity,
                expectedAvailable,
                LocalDate.of(2020, 10, 5),
                LocalDate.of(2020, 10, 5)
        );

        workerService.addAvailability(availabilityRequest);

        Mockito.verify(availabilityRepository).save(ArgumentMatchers.argThat(actual -> {
            Assertions.assertEquals(expectedAvailable, actual);
            return true;
        }));

        Mockito.verify(serviceWorkerAvailabilityRepository).save(ArgumentMatchers.argThat(actual -> {
            Assertions.assertEquals(expectedWorkerAvailable, actual);
            return true;
        }));
    }
}
