package com.rmit.sept.septbackend.service;

import com.rmit.sept.septbackend.entity.*;
import com.rmit.sept.septbackend.model.*;
import com.rmit.sept.septbackend.repository.BookingRepository;
import com.rmit.sept.septbackend.repository.CustomerRepository;
import com.rmit.sept.septbackend.repository.ServiceWorkerAvailabilityRepository;
import com.rmit.sept.septbackend.repository.ServiceWorkerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class BookingServiceTest {
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private ServiceWorkerRepository serviceWorkerRepository;
    @Mock
    private ServiceWorkerAvailabilityRepository serviceWorkerAvailabilityRepository;

    private BookingService bookingService;

    private CustomerEntity testCustomerEntity;
    private CustomerEntity testSecondCustomerEntity;
    private ServiceWorkerEntity testServiceWorkerEntity;
    private BookingEntity expectedBookingEntity;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        bookingService = new BookingService(bookingRepository, customerRepository, serviceWorkerRepository, serviceWorkerAvailabilityRepository);

        /*
         * Represents perfect scenario (all cases passing).
         * It is up to each of the test methods to override an appropriate mocked method to demonstrate different validation.
         * In this basic example, there is:
         * - a single business (Mojang)
         * - a single service (Minecraft) with a duration of 30 minutes
         * - a single worker (Notch) whose availability is 12pm-5pm Thursdays, effective until Thursday 15th October
         * - two customers (Lachlan and Jimmy), although Lachlan is the main one used
         * - a new booking to be made (at 3:30pm on Thursday 15th October)
         * - two existing bookings (one at 12pm, another at 4pm)
         */

        LocalDateTime testTime = LocalDateTime.now();

        testCustomerEntity = new CustomerEntity(
                new UserEntity(
                        "Lachlan",
                        "bort",
                        "Lachlan",
                        "Lachlan",
                        Role.CUSTOMER),
                "String streetAddress",
                "String city",
                State.TAS,
                "String postcode"
        );

        testSecondCustomerEntity = new CustomerEntity(
                new UserEntity(
                        "Jimmy",
                        "youcanthandletheneutronstyle",
                        "Jimmy",
                        "Neutron",
                        Role.CUSTOMER),
                "123 Blast Off Street",
                "Retroville",
                State.TAS,
                "1234"
        );

        testServiceWorkerEntity = new ServiceWorkerEntity(
                new ServiceEntity(
                        new BusinessEntity("Mojang"),
                        "Minecraft",
                        30),
                new WorkerEntity(
                        new UserEntity(
                                "Notch",
                                "Cool",
                                "Marcus",
                                "Pearson",
                                Role.WORKER
                        )
                )
        );

        expectedBookingEntity = new BookingEntity(
                0,
                testServiceWorkerEntity,
                testCustomerEntity,
                LocalDateTime.of(2020, 10, 15, 15, 30),
                testTime,
                testTime,
                Status.ACTIVE
        );

        Mockito.when(customerRepository.getByUserUsername(Mockito.any())).thenReturn(testCustomerEntity);

        Mockito.when(serviceWorkerRepository.getByServiceServiceIdAndWorkerWorkerId(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(testServiceWorkerEntity);

        Mockito.when(serviceWorkerAvailabilityRepository.getAllByServiceWorkerId(
                Mockito.anyInt(), Mockito.any(), Mockito.any()))
                .thenReturn(
                        Collections.singletonList(
                                new ServiceWorkerAvailabilityEntity(
                                        0,
                                        testServiceWorkerEntity,
                                        new AvailabilityEntity(
                                                0,
                                                DayOfWeek.THURSDAY,
                                                LocalTime.of(12, 0),
                                                LocalTime.of(17, 0)
                                        ),
                                        LocalDate.of(2020, 10, 15),
                                        LocalDate.of(2020, 10, 15)
                                )
                        )
                );

        Mockito.when(bookingRepository.getAllByServiceWorkerWorkerWorkerIdAndStatus(Mockito.anyInt(), Mockito.any()))
                .thenReturn(
                        Collections.singletonList(
                                // Booking made by Jimmy - just to show the worker can be occupied by someone else
                                new BookingEntity(
                                        0,
                                        testServiceWorkerEntity,
                                        testSecondCustomerEntity,
                                        LocalDateTime.of(2020, 10, 15, 16, 30),
                                        testTime,
                                        testTime,
                                        Status.ACTIVE
                                )
                        )
                );

        Mockito.when(bookingRepository.getAllByCustomerUserUsernameAndStatus(Mockito.any(), Mockito.any()))
                .thenReturn(
                        Arrays.asList(
                                // Before above booking
                                new BookingEntity(
                                        0,
                                        testServiceWorkerEntity,
                                        testCustomerEntity,
                                        LocalDateTime.of(2020, 10, 15, 12, 0),
                                        testTime,
                                        testTime,
                                        Status.ACTIVE
                                ),
                                // After above booking
                                new BookingEntity(
                                        0,
                                        testServiceWorkerEntity,
                                        testCustomerEntity,
                                        LocalDateTime.of(2020, 10, 15, 16, 0),
                                        testTime,
                                        testTime,
                                        Status.ACTIVE
                                )
                        )
                );
    }

    @Test
    public void cancelBooking() {
        LocalDateTime testTime = LocalDateTime.now();
        Mockito.when(bookingRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(expectedBookingEntity));

        BookingEntity expected = new BookingEntity(
                expectedBookingEntity.getBookingId(),
                testServiceWorkerEntity,
                testCustomerEntity,
                LocalDateTime.of(2020, 10, 15, 15, 30),
                testTime,
                testTime,
                Status.CANCELLED
        );

        bookingService.cancelBooking(0);
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
        BookingRequest br = new BookingRequest(0, 0, "Lachlan",
                LocalDateTime.of(2020, 10, 15, 15, 30));

        bookingService.createBooking(br);

        Mockito.verify(bookingRepository).save(ArgumentMatchers.argThat(actual -> {
            Assertions.assertEquals(expectedBookingEntity, actual);
            return true;
        }));
    }

    @Test
    public void createBookingWorkerNotAvailable() {
        BookingRequest br = new BookingRequest(0, 0, "Lachlan",
                LocalDateTime.of(2020, 10, 14, 15, 30));

        Assertions.assertThrows(ResponseStatusException.class, () -> bookingService.createBooking(br));
    }

    @Test
    public void createBookingWorkerNotAvailableEffectiveDates() {
        BookingRequest br = new BookingRequest(0, 0, "Lachlan",
                LocalDateTime.of(2020, 10, 22, 15, 30));

        Mockito.when(serviceWorkerAvailabilityRepository.getAllByServiceWorkerId(
                Mockito.anyInt(), Mockito.any(), Mockito.any()))
                .thenReturn(new ArrayList<>());

        Assertions.assertThrows(ResponseStatusException.class, () -> bookingService.createBooking(br));
    }

    @Test
    public void createBookingWorkerOverlap() {
        Mockito.when(bookingRepository.getAllByServiceWorkerWorkerWorkerIdAndStatus(Mockito.anyInt(), Mockito.any()))
                .thenReturn(
                        Collections.singletonList(
                                new BookingEntity(0,
                                        testServiceWorkerEntity,
                                        testSecondCustomerEntity,
                                        // This is the overlap (3:29pm)
                                        LocalDateTime.of(2020, 10, 15, 15, 29),
                                        LocalDateTime.now(),
                                        LocalDateTime.now(),
                                        Status.ACTIVE
                                )
                        )
                );

        BookingRequest br = new BookingRequest(0, 0, "Lachlan",
                LocalDateTime.of(2020, 10, 15, 15, 30));

        Assertions.assertThrows(ResponseStatusException.class, () -> bookingService.createBooking(br));
    }

    @Test
    public void createBookingCustomerOverlap() {
        BookingRequest br = new BookingRequest(1, 0, "Lachlan",
                LocalDateTime.of(2020, 10, 15, 15, 31));

        Assertions.assertThrows(ResponseStatusException.class, () -> bookingService.createBooking(br));
    }

    @Test
    public void createBookingCustomerDoesntExist() {
        Mockito.when(customerRepository.getByUserUsername(Mockito.any())).thenReturn(null);

        BookingRequest br = new BookingRequest(0, 0, "DoesntExist",
                LocalDateTime.of(2020, 10, 15, 15, 30)
        );

        Assertions.assertThrows(ResponseStatusException.class, () -> bookingService.createBooking(br));
    }

    @Test
    public void testViewBookings() {
        Mockito.when(bookingRepository.getAllByCustomerUserUsernameAndStatus(Mockito.any(), Mockito.any()))
                .thenReturn(Collections.singletonList(expectedBookingEntity));

        List<BookingResponse> expected = Collections.singletonList(
                new BookingResponse(
                        "Minecraft",
                        "Marcus Pearson",
                        LocalDateTime.of(2020, 10, 15, 15, 30),
                        0,
                        "Notch"
                )
        );

        List<BookingResponse> actual = bookingService.viewBookings("Notch", Status.ACTIVE);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testNoBookings() {
        Mockito.when(bookingRepository.getAllByCustomerUserUsernameAndStatus(Mockito.any(), Mockito.any()))
                .thenReturn(new ArrayList<>());

        List<BookingResponse> expected = new ArrayList<>();

        List<BookingResponse> actual = bookingService.viewBookings("Lucas", Status.ACTIVE);


        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testNoLogin() {
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
    public void testMultipleBookings() {
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
                new BookingResponse("Minecraft", "Marcus Pearson", LocalDateTime.of(2020, 10, 15, 15, 30), 0, "Notch"),
                new BookingResponse("Minecraft", "Marcus Pearson", LocalDateTime.of(2020, 10, 16, 15, 30), 0, "Notch"));


        List<BookingResponse> actual = bookingService.viewBookings("Notch", Status.ACTIVE);


        Assertions.assertEquals(expected, actual);
    }
}
