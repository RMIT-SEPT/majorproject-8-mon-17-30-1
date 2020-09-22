package com.rmit.sept.septbackend.service;

import com.rmit.sept.septbackend.entity.*;
import com.rmit.sept.septbackend.model.*;
import com.rmit.sept.septbackend.repository.BookingRepository;
import com.rmit.sept.septbackend.repository.CustomerRepository;
import com.rmit.sept.septbackend.repository.ServiceWorkerRepository;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;
    private BookingService bookingService;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private ServiceWorkerRepository serviceWorkerRepository;

    List<BookingEntity> getAllByCustomer;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        bookingService = new BookingService(bookingRepository, customerRepository, serviceWorkerRepository);
    }

    @BeforeEach
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
        LocalDateTime testTime = LocalDateTime.now();
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
                testTime,
                testTime,
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
                testTime,
                testTime,
                Status.CANCELLED);

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
