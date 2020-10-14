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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ServiceServiceTests {

    @Mock
    private ServiceRepository serviceRepository;
    @Mock
    private BusinessRepository businessRepository;
    @Mock
    private ServiceWorkerRepository serviceWorkerRepository;
    @Mock
    private WorkerRepository workerRepository;
    @Mock
    private ServiceWorkerAvailabilityRepository serviceWorkerAvailabilityRepository;
    @Mock
    private AvailabilityRepository availabilityRepository;

    private ServiceService serviceService;

    @BeforeAll
    public void setup() {
        MockitoAnnotations.initMocks(this);
        serviceService = new ServiceService(serviceRepository, businessRepository, serviceWorkerRepository, workerRepository, serviceWorkerAvailabilityRepository, availabilityRepository);
    }

    @Test
    public void testGetServicesForBusinessId() {
        Mockito.when(businessRepository.existsById(Mockito.any()))
                .thenReturn(true);
        Mockito.when(serviceRepository.getAllByBusinessBusinessIdAndStatus(Mockito.any(), Mockito.any()))
                .thenReturn(
                        Arrays.asList(
                                new ServiceEntity(0, new BusinessEntity(0, "testBusiness"), "testService", 60, Status.ACTIVE),
                                new ServiceEntity(1, new BusinessEntity(0, "testBusiness"), "anotherTestService", 124235, Status.ACTIVE)
                        )
                );

        List<ServiceResponse> expected = Arrays.asList(
                new ServiceResponse(0, "testBusiness", "testService", 60),
                new ServiceResponse(1, "testBusiness", "anotherTestService", 124235)
        );

        List<ServiceResponse> actual = serviceService.getServicesForBusinessId(0).getBody();

        Assertions.assertEquals(expected, actual);
    }


    @Test
    public void testGetServicesForUsername() {
        Mockito.when(workerRepository.existsByUserUsername(Mockito.any()))
                .thenReturn(true);
        Mockito.when(serviceWorkerRepository.getAllByWorkerUserUsernameAndServiceStatusAndWorkerStatus(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(
                        Arrays.asList(
                                new ServiceWorkerEntity(
                                        new ServiceEntity(0, new BusinessEntity("testBusiness"), "testService", 60, Status.ACTIVE),
                                        new WorkerEntity(new UserEntity("testUsername", "testPassword", "testFirst", "testLast", Role.WORKER))),
                                new ServiceWorkerEntity(
                                        new ServiceEntity(1, new BusinessEntity("testBusiness"), "anotherTestService", 124235, Status.ACTIVE),
                                        new WorkerEntity(new UserEntity("testUsername", "testPassword", "testFirst", "testLast", Role.WORKER)))
                        )
                );

        List<ServiceResponse> expected = Arrays.asList(
                new ServiceResponse(0, "testBusiness", "testService", 60),
                new ServiceResponse(1, "testBusiness", "anotherTestService", 124235)
        );

        List<ServiceResponse> actual = serviceService.getServicesForUsername("testUsername").getBody();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testGetServicesForBusinessNameDoesntExist() {
        Mockito.when(businessRepository.existsByBusinessName(Mockito.any()))
                .thenReturn(false);
        ValidationResponse<List<ServiceResponse>> actual = serviceService.getServicesForBusinessId(0);
        Assertions.assertFalse(actual.isSuccessful());
    }

    @Test
    public void testGetServicesForUsernameDoesntExist() {
        Mockito.when(workerRepository.existsByUserUsername(Mockito.any()))
                .thenReturn(false);
        ValidationResponse<List<ServiceResponse>> actual = serviceService.getServicesForUsername("testUsername");
        Assertions.assertFalse(actual.isSuccessful());
    }

    @Test
    public void testEditService() {
        Mockito.when(serviceRepository.findById(Mockito.anyInt())).thenReturn(
                Optional.of(new ServiceEntity(
                                0,
                                new BusinessEntity(0, "testBusiness"),
                                "testService",
                                60,
                                Status.ACTIVE
                        )
                )
        );

        CreateServiceRequest editRequest = new CreateServiceRequest(0, "newServiceName", 120);

        ServiceEntity expected = new ServiceEntity(
                0,
                new BusinessEntity(0, "testBusiness"),
                "newServiceName",
                120,
                Status.ACTIVE
        );

        serviceService.editService(0, editRequest);
        Mockito.verify(serviceRepository).save(ArgumentMatchers.argThat(actual -> {
            Assertions.assertEquals(expected, actual);
            return true;
        }));
    }
}