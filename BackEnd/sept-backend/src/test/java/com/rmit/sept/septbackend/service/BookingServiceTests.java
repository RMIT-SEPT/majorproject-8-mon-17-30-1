package com.rmit.sept.septbackend.service;

import com.rmit.sept.septbackend.entity.*;
import com.rmit.sept.septbackend.model.BookingResponse;
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
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookingServiceTests {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private ServiceWorkerRepository serviceWorkerRepository;
    private BookingService bookingService;

    @BeforeAll
    public void setup() {
        MockitoAnnotations.initMocks(this);
        bookingService = new BookingService(bookingRepository, customerRepository, serviceWorkerRepository);
    }

    @Test
    public void testViewBookings() {
        Mockito.when(bookingRepository.getAllByCustomerUserUsernameAndStatus(Mockito.any(), Mockito.any()))
                .thenReturn(Arrays.asList(
                        new BookingEntity(
                                new ServiceWorkerEntity(
                                        new ServiceEntity(
                                                new BusinessEntity("Mojang"),
                                                "Minecraft",
                                                180
                                        ),
                                        new WorkerEntity(
                                                new UserEntity(
                                                        "Notch",
                                                        "Cool",
                                                        "Marcus",
                                                        "Pearson",
                                                        Role.WORKER
                                                )
                                        )
                                ),
                                new CustomerEntity(
                                        new UserEntity(
                                                "Lachlan",
                                                "bort",
                                                "Lachlan",
                                                "Lachlan",
                                                Role.CUSTOMER
                                        ),
                                        "String streetAddress",
                                        "String city",
                                        State.TAS,
                                        "String postcode"
                                ),
                                LocalDateTime.of(2020, 10, 15, 15, 30)
                        )
                        )
                );

        List<BookingResponse> expected = Arrays.asList(
                new BookingResponse(
                        "Minecraft",
                        "Marcus Pearson",
                        LocalDateTime.of(2020, 10, 15, 15, 30),
                        0
                )
        );

        List<BookingResponse> actual = bookingService.viewBookings("Notch", Status.ACTIVE);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void TestNoBookings() {
        Mockito.when(bookingRepository.getAllByCustomerUserUsernameAndStatus(Mockito.any(), Mockito.any()))
                .thenReturn(new ArrayList<>());

        List<BookingResponse> expected = new ArrayList<>();

        List<BookingResponse> actual = bookingService.viewBookings("Lucas", Status.ACTIVE);


        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void TestNoLogin() {
        Mockito.when(bookingRepository.getAllByCustomerUserUsernameAndStatus(Mockito.any(), Mockito.any()))
                .thenReturn(Arrays.asList(
                        new BookingEntity
                                (new ServiceWorkerEntity
                                        (new ServiceEntity(
                                                new BusinessEntity("Mojang")
                                                , "Minecraft"
                                                , 180)
                                                , new WorkerEntity(
                                                new UserEntity("Notch", "Cool", "Marcus", "Pearson", Role.WORKER)))
                                        , new CustomerEntity(
                                        new UserEntity("Lachlan", "bort", "Lachlan", "Lachlan", Role.CUSTOMER),
                                        "String streetAddress", "String city", State.TAS, "String postcode"),
                                        LocalDateTime.of(2020, 10, 15, 15, 30)
                                )));

        List<BookingResponse> expected = new ArrayList<>();

        List<BookingResponse> actual = new ArrayList<>();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void TestMultipleBookings() {
        Mockito.when(bookingRepository.getAllByCustomerUserUsernameAndStatus(Mockito.any(), Mockito.any()))
                .thenReturn(Arrays.asList(
                        new BookingEntity
                                (new ServiceWorkerEntity
                                        (new ServiceEntity(
                                                new BusinessEntity("Mojang")
                                                , "Minecraft"
                                                , 180)
                                                , new WorkerEntity(
                                                new UserEntity("Notch", "Cool", "Marcus", "Pearson", Role.WORKER)))
                                        , new CustomerEntity(
                                        new UserEntity("Lachlan", "bort", "Lachlan", "Lachlan", Role.CUSTOMER),
                                        "String streetAddress", "String city", State.TAS, "String postcode"), LocalDateTime.of(2020, 10, 15, 15, 30)
                                ),
                        new BookingEntity
                                (new ServiceWorkerEntity
                                        (new ServiceEntity(
                                                new BusinessEntity("Mojang")
                                                , "Minecraft"
                                                , 90)
                                                , new WorkerEntity(
                                                new UserEntity("Notch", "Cool", "Marcus", "Pearson", Role.WORKER)))
                                        , new CustomerEntity(
                                        new UserEntity("Lachlan", "bort", "Lachlan", "Lachlan", Role.CUSTOMER),
                                        "String streetAddress", "String city", State.TAS, "String postcode"), LocalDateTime.of(2020, 10, 16, 15, 30)
                                )));

        List<BookingResponse> expected = Arrays.asList(
                new BookingResponse("Minecraft", "Marcus Pearson", LocalDateTime.of(2020, 10, 15, 15, 30), 0),
                new BookingResponse("Minecraft", "Marcus Pearson", LocalDateTime.of(2020, 10, 16, 15, 30), 0));


        List<BookingResponse> actual = bookingService.viewBookings("Notch", Status.ACTIVE);


        Assertions.assertEquals(expected, actual);
    }
}
