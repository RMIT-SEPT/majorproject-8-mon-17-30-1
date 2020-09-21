package com.rmit.sept.septbackend.service;

import com.rmit.sept.septbackend.entity.BookingEntity;
import com.rmit.sept.septbackend.entity.CustomerEntity;
import com.rmit.sept.septbackend.entity.ServiceWorkerEntity;
import com.rmit.sept.septbackend.entity.UserEntity;
import com.rmit.sept.septbackend.model.BookingRequest;
import com.rmit.sept.septbackend.model.BookingResponse;
import com.rmit.sept.septbackend.model.Status;
import com.rmit.sept.septbackend.repository.BookingRepository;
import com.rmit.sept.septbackend.repository.CustomerRepository;
import com.rmit.sept.septbackend.repository.ServiceWorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final ServiceWorkerRepository serviceWorkerRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository, CustomerRepository customerRepository, ServiceWorkerRepository serviceWorkerRepository) {
        this.bookingRepository = bookingRepository;
        this.customerRepository = customerRepository;
        this.serviceWorkerRepository = serviceWorkerRepository;
    }

    public List<BookingResponse> viewBookings(String username, Status status) {
        List<BookingEntity> bookingEntities = bookingRepository.getAllByCustomerUserUsernameAndStatus(username, status);

        return bookingEntities.stream().map(bookingEntity -> {
            UserEntity userEntity = bookingEntity.getServiceWorker().getWorker().getUser();

            return new BookingResponse(
                    bookingEntity.getServiceWorker().getService().getServiceName(),
                    userEntity.getFirstName() + " " + userEntity.getLastName(),
                    bookingEntity.getBookingTime(),
                    bookingEntity.getBookingId()
            );
        }).collect(Collectors.toList()
        );
    }

    public List<BookingResponse> viewAllPastBookings() {

        List<BookingEntity> bookingEntities = new ArrayList<>();
        for (BookingEntity bookingEntity : bookingRepository.findAll()) {
            if (bookingEntity.getBookingTime().isBefore(LocalDateTime.now()))
                bookingEntities.add(bookingEntity);
        }

        return bookingEntities.stream().map(bookingEntity -> {
            UserEntity userEntity = bookingEntity.getServiceWorker().getWorker().getUser();

            return new BookingResponse(
                    bookingEntity.getServiceWorker().getService().getServiceName(),
                    userEntity.getFirstName() + " " + userEntity.getLastName(),
                    bookingEntity.getBookingTime(),
                    bookingEntity.getBookingId()
            );
        }).collect(Collectors.toList()
        );
    }

    public void createBooking(BookingRequest bookingRequest) {
        CustomerEntity customerEntity = customerRepository.getByUserUsername(bookingRequest.getCustomerUsername());
        ServiceWorkerEntity serviceWorkerEntity = serviceWorkerRepository.getByServiceServiceIdAndWorkerWorkerId(bookingRequest.getServiceId(), bookingRequest.getWorkerId());

        //validation
        //check if the serviceworker isn't booked for that time
        List<BookingEntity> serviceBookings = bookingRepository.getAllByServiceWorkerWorkerWorkerIdAndStatus(bookingRequest.getWorkerId(), Status.ACTIVE);
        for (BookingEntity be : serviceBookings) {
            if (bookingRequest.getBookingTime().plusMinutes(be.getServiceWorker().getService().getDurationMinutes()).isBefore(be.getBookingTime())
                    || bookingRequest.getBookingTime().isAfter(be.getBookingTime().plusMinutes(be.getServiceWorker().getService().getDurationMinutes()))) {

            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Service worker is busy");
            }
        }

        List<BookingEntity> customerBookings = bookingRepository.getAllByCustomerUserUsernameAndStatus(bookingRequest.getCustomerUsername(), Status.ACTIVE);
        for (BookingEntity be : customerBookings) {
            if (bookingRequest.getBookingTime().plusMinutes(be.getServiceWorker().getService().getDurationMinutes()).isBefore(be.getBookingTime())
                    || bookingRequest.getBookingTime().isAfter(be.getBookingTime().plusMinutes(be.getServiceWorker().getService().getDurationMinutes()))) {

            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer is busy");
            }
        }

        if (!customerRepository.existsByUserUsername(bookingRequest.getCustomerUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer doesn't exist.");
        }

        BookingEntity bookingEntity = new BookingEntity(
                serviceWorkerEntity,
                customerEntity,
                bookingRequest.getBookingTime()
        );

        bookingRepository.save(bookingEntity);

        //check if the customer entity isn't booked for that time

    }

    public void cancelBooking(int bookingId) {
        Optional<BookingEntity> entity = bookingRepository.findById(bookingId);
        if (entity.isPresent()) {
            BookingEntity bookingEntity = entity.get();
            bookingEntity.setStatus(Status.CANCELLED);
            bookingRepository.save(bookingEntity);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Booking not found");
        }
    }

}
