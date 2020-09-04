package com.rmit.sept.septbackend.service;

import com.rmit.sept.septbackend.entity.BookingEntity;
import com.rmit.sept.septbackend.entity.UserEntity;
import com.rmit.sept.septbackend.entity.WorkerEntity;
import com.rmit.sept.septbackend.model.BookingRequest;
import com.rmit.sept.septbackend.model.BookingResponse;
import com.rmit.sept.septbackend.model.Status;
import com.rmit.sept.septbackend.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    @Autowired
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public List<BookingResponse> viewBookings(String username) {
        List<BookingEntity> bookingEntities = bookingRepository.getAllByCustomerUserUsernameAndStatus(username, Status.ACTIVE);

        return bookingEntities.stream().map(bookingEntity -> {
            UserEntity userEntity = bookingEntity.getServiceWorker().getWorker().getUser();

            return new BookingResponse(
                    bookingEntity.getServiceWorker().getService().getServiceName(),
                    userEntity.getFirstName() + " " + userEntity.getLastName(),
                    bookingEntity.getBookingTime()
            );
        }).collect(Collectors.toList()
        );

    }

}
