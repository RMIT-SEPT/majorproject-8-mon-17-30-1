package com.rmit.sept.septbackend.service;

import com.rmit.sept.septbackend.entity.*;
import com.rmit.sept.septbackend.model.*;
import com.rmit.sept.septbackend.repository.BookingRepository;
import com.rmit.sept.septbackend.repository.CustomerRepository;
import com.rmit.sept.septbackend.repository.ServiceWorkerRepository;
import org.h2.engine.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;
    private BookingService bookingService;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private ServiceWorkerRepository serviceWorkerRepository;

    @BeforeAll
    public void setup() {
        MockitoAnnotations.initMocks(this);
        bookingService = new BookingService(bookingRepository, customerRepository, serviceWorkerRepository);
    }

    //    @Test
//    public void testViewBookings() {
//        Mockito.when(bookingRepository.getAllByCustomerUserUsernameAndStatus(Mockito.any(), Mockito.any()))
//                .thenReturn(Arrays.asList(
//                        new BookingEntity(new ServiceWorkerEntity(new ServiceEntity())
//                ))
//    }


    @Test
    public void createBooking() {
        Mockito.when(customerRepository.getByUserUsername(Mockito.any()))
                .thenReturn(new CustomerEntity(
                        new UserEntity("Lachlan", "bort", "Lachlan", "Lachlan", Role.CUSTOMER),
                        "String streetAddress", "String city", State.TAS, "String postcode"));

        Mockito.when(serviceWorkerRepository.getByServiceServiceIdAndWorkerWorkerId(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(new ServiceWorkerEntity
                        (new ServiceEntity(
                                new BusinessEntity("Mojang")
                                , "Minecraft"
                                , 180)
                                , new WorkerEntity(
                                new UserEntity("Notch", "Cool", "Marcus", "Pearson", Role.WORKER))));

        BookingRequest br = new BookingRequest(0, 0, "Lachlan",
                LocalDateTime.of(2020, 10, 15, 15, 30));

        BookingEntity expected = new BookingEntity(0, new ServiceWorkerEntity
                (new ServiceEntity(
                        new BusinessEntity("Mojang")
                        , "Minecraft"
                        , 180)
                        , new WorkerEntity(
                        new UserEntity("Notch", "Cool", "Marcus", "Pearson", Role.WORKER))), new CustomerEntity(
                new UserEntity("Lachlan", "bort", "Lachlan", "Lachlan", Role.CUSTOMER),
                "String streetAddress", "String city", State.TAS, "String postcode"),
                LocalDateTime.of(2020, 10, 15, 15, 30),
                LocalDateTime.now(),
                LocalDateTime.now(),
                Status.ACTIVE);

        bookingService.createBooking(br);

        Mockito.verify(bookingRepository).save(ArgumentMatchers.argThat(actual -> {
            Assertions.assertEquals(expected, actual);
            return true;
        }));
    }

    @Test
    public void createBookingBusy() {
        Mockito.when(customerRepository.getByUserUsername(Mockito.any()))
                .thenReturn(new CustomerEntity(
                        new UserEntity("Lachlan", "bort", "Lachlan", "Lachlan", Role.CUSTOMER),
                        "String streetAddress", "String city", State.TAS, "String postcode"));

        Mockito.when(serviceWorkerRepository.getByServiceServiceIdAndWorkerWorkerId(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(new ServiceWorkerEntity
                        (new ServiceEntity(
                                new BusinessEntity("Mojang")
                                , "Minecraft"
                                , 30)
                                , new WorkerEntity(
                                new UserEntity("Notch", "Cool", "Marcus", "Pearson", Role.WORKER))));

        Mockito.when(bookingRepository.getAllByCustomerUserUsernameAndStatus(Mockito.any(), Mockito.any()))
                .thenReturn(Arrays.asList(new BookingEntity(0, new ServiceWorkerEntity
                        (new ServiceEntity(
                                new BusinessEntity("Mojang")
                                , "Minecraft"
                                , 30)
                                , new WorkerEntity(0,
                                new UserEntity("Notch", "Cool", "Marcus", "Pearson", Role.WORKER))), new CustomerEntity(
                        new UserEntity("Lachlan", "bort", "Lachlan", "Lachlan", Role.CUSTOMER),
                        "String streetAddress", "String city", State.TAS, "String postcode"),
                        LocalDateTime.of(2020, 10, 15, 15, 30),
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        Status.ACTIVE)));

        BookingRequest br = new BookingRequest(0, 0, "Lachlan", LocalDateTime.of(2020, 10, 15, 15, 30)
        );

        BookingEntity expected = new BookingEntity(0, new ServiceWorkerEntity
                (new ServiceEntity(
                        new BusinessEntity("Mojang")
                        , "Minecraft"
                        , 180)
                        , new WorkerEntity(
                        new UserEntity("Notch", "Cool", "Marcus", "Pearson", Role.WORKER))), new CustomerEntity(
                new UserEntity("Lachlan", "bort", "Lachlan", "Lachlan", Role.CUSTOMER),
                "String streetAddress", "String city", State.TAS, "String postcode"),
                LocalDateTime.of(2020, 10, 15, 15, 30),
                LocalDateTime.now(),
                LocalDateTime.now(),
                Status.ACTIVE);

        Assertions.assertThrows(ResponseStatusException.class, () -> bookingService.createBooking(br));

    }
}
