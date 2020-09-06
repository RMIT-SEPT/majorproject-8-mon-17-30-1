package com.rmit.sept.septbackend.service;

import com.rmit.sept.septbackend.entity.BookingEntity;
import com.rmit.sept.septbackend.model.BookingRequest;
import com.rmit.sept.septbackend.repository.BookingRepository;
import com.rmit.sept.septbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public void createBooking(BookingRequest bookingRequest) {
        BookingEntity bookingEntity = new BookingEntity(
                bookingRequest.getBookingId(),
                bookingRequest.getServiceWorkerId(),
                bookingRequest.getCustomerId(),
                bookingRequest.getBookingTime(),
                bookingRequest.getCreatedTime(),
                bookingRequest.getLastModifiedTime(),
                bookingRequest.getStatus()
        );

        bookingRepository.save(bookingEntity);
    }
}
