package com.rmit.sept.septbackend.service;

import com.rmit.sept.septbackend.entity.BookingEntity;
import com.rmit.sept.septbackend.entity.ServiceEntity;
import com.rmit.sept.septbackend.entity.ServiceWorkerEntity;
import com.rmit.sept.septbackend.repository.BookingRepository;
import com.rmit.sept.septbackend.repository.CustomerRepository;
import com.rmit.sept.septbackend.repository.ServiceWorkerRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.Arrays;

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


}
