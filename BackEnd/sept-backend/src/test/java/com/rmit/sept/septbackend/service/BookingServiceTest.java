package com.rmit.sept.septbackend.service;

import com.rmit.sept.septbackend.entity.*;
import com.rmit.sept.septbackend.model.BookingRequest;
import com.rmit.sept.septbackend.model.Role;
import com.rmit.sept.septbackend.model.State;
import com.rmit.sept.septbackend.model.Status;
import com.rmit.sept.septbackend.repository.BookingRepository;
import com.rmit.sept.septbackend.repository.CustomerRepository;
import com.rmit.sept.septbackend.repository.ServiceWorkerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

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

    List<BookingEntity> getAllByCustomer;

    @BeforeAll
    public void setup() {
        MockitoAnnotations.initMocks(this);
        bookingService = new BookingService(bookingRepository, customerRepository, serviceWorkerRepository);
    }

    @BeforeAll
    public void setMocks() {
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

        getAllByCustomer = Arrays.asList(new BookingEntity(0, new ServiceWorkerEntity
                        (new ServiceEntity(
                                new BusinessEntity(0,"Mojang")
                                , "Minecraft"
                                , 30)
                                , new WorkerEntity(0,
                                new UserEntity("Notch", "Cool", "Marcus", "Pearson", Role.WORKER))), new CustomerEntity(
                        new UserEntity("Lachlan", "bort", "Lachlan", "Lachlan", Role.CUSTOMER),
                        "String streetAddress", "String city", State.TAS, "String postcode"),
                        LocalDateTime.of(2020, 10, 15, 15, 30),
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        Status.ACTIVE));

        Mockito.when(bookingRepository.getAllByCustomerUserUsernameAndStatus(Mockito.any(), Mockito.any()))
                .thenReturn(getAllByCustomer);
    }

    @Test
    public void cancelBooking() {
        Mockito.when(bookingRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(new BookingEntity(0, new ServiceWorkerEntity
                (new ServiceEntity(
                        new BusinessEntity(0, "Mojang")
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

        BookingEntity expected = new BookingEntity(0, new ServiceWorkerEntity
                (new ServiceEntity(
                        new BusinessEntity(0,"Mojang")
                        , "Minecraft"
                        , 30)
                        , new WorkerEntity(0,
                        new UserEntity("Notch", "Cool", "Marcus", "Pearson", Role.WORKER))), new CustomerEntity(
                new UserEntity("Lachlan", "bort", "Lachlan", "Lachlan", Role.CUSTOMER),
                "String streetAddress", "String city", State.TAS, "String postcode"),
                LocalDateTime.of(2020, 10, 15, 15, 30),
                LocalDateTime.now(),
                LocalDateTime.now(),
                Status.ACTIVE);

        Mockito.verify(bookingRepository).save(ArgumentMatchers.argThat(actual -> {
            Assertions.assertEquals(expected, actual);
            return true;
        }));
    }

    @Test
    public void cancelBookingNotFound() {
        Mockito.when(bookingRepository.findById(Mockito.any())).thenReturn(
                Optional.empty()
        );
        Assertions.assertThrows(ResponseStatusException.class, () -> bookingService.cancelBooking(3));
    }

    @Test
    public void createBooking() {
        Mockito.when(customerRepository.existsByUserUsername(Mockito.any()))
                .thenReturn(getAllByCustomer.stream().anyMatch(a -> a.getCustomer().getUser().getUsername().equals("Lachlan")));

        BookingRequest br = new BookingRequest(0, 0, "Lachlan",
                LocalDateTime.of(2020, 11, 15, 20, 30));

        BookingEntity expected = new BookingEntity(0, new ServiceWorkerEntity
                (new ServiceEntity(
                        new BusinessEntity("Mojang")
                        , "Minecraft"
                        , 30)
                        , new WorkerEntity(
                        new UserEntity("Notch", "Cool", "Marcus", "Pearson", Role.WORKER))), new CustomerEntity(
                new UserEntity("Lachlan", "bort", "Lachlan", "Lachlan", Role.CUSTOMER),
                "String streetAddress", "String city", State.TAS, "String postcode"),
                LocalDateTime.of(2020, 11, 15, 20, 30),
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
    public void createBookingWorkerBusy() {
        Mockito.when(customerRepository.existsByUserUsername(Mockito.any()))
                .thenReturn(getAllByCustomer.stream().anyMatch(a -> a.getCustomer().getUser().getUsername().equals("Lachlan")));

        Mockito.when(bookingRepository.getAllByServiceWorkerWorkerWorkerIdAndStatus(Mockito.anyInt(), Mockito.any()))
                .thenReturn(Arrays.asList(new BookingEntity(0, new ServiceWorkerEntity
                        (new ServiceEntity(
                                new BusinessEntity(0,"Mojang")
                                , "Minecraft"
                                , 30)
                                , new WorkerEntity(0,
                                new UserEntity("Notch", "Cool", "Marcus", "Pearson", Role.WORKER))), new CustomerEntity(
                        new UserEntity("Jerry", "bort", "Lachlan", "Lachlan", Role.CUSTOMER),
                        "String streetAddress", "String city", State.TAS, "String postcode"),
                        LocalDateTime.of(2020, 10, 15, 15, 30),
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        Status.ACTIVE)));

        Mockito.when(customerRepository.existsByUserUsername(Mockito.any()))
                .thenReturn(true);

        BookingRequest br = new BookingRequest(0, 0, "Lachlan",
                LocalDateTime.of(2020, 10, 15, 15, 30));

        Assertions.assertThrows(ResponseStatusException.class, () -> bookingService.createBooking(br));

    }

    @Test
    public void createBookingCustomerBusy() {
        Mockito.when(customerRepository.existsByUserUsername(Mockito.any()))
                .thenReturn(getAllByCustomer.stream().anyMatch(a -> a.getCustomer().getUser().getUsername().equals("Lachlan")));

        BookingRequest br = new BookingRequest(1, 0, "Lachlan",
                LocalDateTime.of(2020, 10, 15, 15, 30));

        Assertions.assertThrows(ResponseStatusException.class, () -> bookingService.createBooking(br));
    }

    @Test
    public void createBookingCustomerNotExist() {
        Mockito.when(customerRepository.existsByUserUsername(Mockito.any()))
                .thenReturn(getAllByCustomer.stream().anyMatch(a -> a.getCustomer().getUser().getUsername().equals("DoesntExist")));

        BookingRequest br = new BookingRequest(0, 0, "DoesntExist",
                LocalDateTime.of(2020, 10, 15, 15, 30)
        );

        Assertions.assertThrows(ResponseStatusException.class, () -> bookingService.createBooking(br));
    }
}
