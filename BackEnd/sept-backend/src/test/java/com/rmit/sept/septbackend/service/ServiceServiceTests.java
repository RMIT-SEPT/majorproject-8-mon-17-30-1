package com.rmit.sept.septbackend.service;

import com.rmit.sept.septbackend.entity.*;
import com.rmit.sept.septbackend.model.Role;
import com.rmit.sept.septbackend.model.ServiceResponse;
import com.rmit.sept.septbackend.repository.BusinessRepository;
import com.rmit.sept.septbackend.repository.ServiceRepository;
import com.rmit.sept.septbackend.repository.ServiceWorkerRepository;
import com.rmit.sept.septbackend.repository.WorkerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

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

    private ServiceService serviceService;

    @BeforeAll
    public void setup() {
        MockitoAnnotations.initMocks(this);
        serviceService = new ServiceService(serviceRepository, businessRepository, serviceWorkerRepository, workerRepository);
    }

    @Test
    public void testGetServicesForBusinessId() {
        Mockito.when(businessRepository.existsById(Mockito.any()))
                .thenReturn(true);
        Mockito.when(serviceRepository.getAllByBusinessBusinessId(Mockito.any()))
                .thenReturn(
                        Arrays.asList(
                                new ServiceEntity(new BusinessEntity(0, "testBusiness"), "testService", 60),
                                new ServiceEntity(new BusinessEntity(0, "testBusiness"), "anotherTestService", 124235)
                        )
                );

        List<ServiceResponse> expected = Arrays.asList(
                new ServiceResponse("testBusiness", "testService", 60),
                new ServiceResponse("testBusiness", "anotherTestService", 124235)
        );

        List<ServiceResponse> actual = serviceService.getServicesForBusinessId(0);

        Assertions.assertEquals(expected, actual);
    }


    @Test
    public void testGetServicesForUsername() {
        Mockito.when(workerRepository.existsByUserUsername(Mockito.any()))
                .thenReturn(true);
        Mockito.when(serviceWorkerRepository.getAllByWorkerUserUsername(Mockito.any()))
                .thenReturn(
                        Arrays.asList(
                                new ServiceWorkerEntity(
                                        new ServiceEntity(new BusinessEntity("testBusiness"), "testService", 60),
                                        new WorkerEntity(new UserEntity("testUsername", "testPassword", "testFirst", "testLast", Role.WORKER))),
                                new ServiceWorkerEntity(
                                        new ServiceEntity(new BusinessEntity("testBusiness"), "anotherTestService", 124235),
                                        new WorkerEntity(new UserEntity("testUsername", "testPassword", "testFirst", "testLast", Role.WORKER)))
                        )
                );

        List<ServiceResponse> expected = Arrays.asList(
                new ServiceResponse("testBusiness", "testService", 60),
                new ServiceResponse("testBusiness", "anotherTestService", 124235)
        );

        List<ServiceResponse> actual = serviceService.getServicesForUsername("testUsername");

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testGetServicesForBusinessNameDoesntExist() {
        Mockito.when(businessRepository.existsByBusinessName(Mockito.any()))
                .thenReturn(false);

        Assertions.assertThrows(ResponseStatusException.class, () -> serviceService.getServicesForBusinessId(0));
    }

    @Test
    public void testGetServicesForUsernameDoesntExist() {
        Mockito.when(workerRepository.existsByUserUsername(Mockito.any()))
                .thenReturn(false);

        Assertions.assertThrows(ResponseStatusException.class, () -> serviceService.getServicesForUsername("testUsername"));
    }
}