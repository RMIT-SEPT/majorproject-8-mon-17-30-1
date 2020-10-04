package com.rmit.sept.septbackend.service;

import com.rmit.sept.septbackend.entity.*;
import com.rmit.sept.septbackend.model.BookingRequest;
import com.rmit.sept.septbackend.model.BookingResponse;
import com.rmit.sept.septbackend.model.Status;
import com.rmit.sept.septbackend.repository.BookingRepository;
import com.rmit.sept.septbackend.repository.CustomerRepository;
import com.rmit.sept.septbackend.repository.ServiceWorkerAvailabilityRepository;
import com.rmit.sept.septbackend.repository.ServiceWorkerRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor(onConstructor_ = {@Autowired})
@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final ServiceWorkerRepository serviceWorkerRepository;
    private final ServiceWorkerAvailabilityRepository serviceWorkerAvailabilityRepository;

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

        // Validation performed (in order):
        //     - Check if the customer exists
        //     - Check if the service/worker relationship exists
        //     - Check if the worker has allocated availability for the service
        //     - Check if the worker does not have an overlapping booking
        //     - Check if the customer does not have an overlapping booking

        // Check if the customer exists
        CustomerEntity customerEntity = customerRepository.getByUserUsername(bookingRequest.getCustomerUsername());
        if (customerEntity == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer doesn't exist");
        }

        // Check if the service/worker relationship exists
        ServiceWorkerEntity serviceWorkerEntity = serviceWorkerRepository.getByServiceServiceIdAndWorkerWorkerId(bookingRequest.getServiceId(), bookingRequest.getWorkerId());
        if (serviceWorkerEntity == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Service/worker relationship doesn't exist");
        }

        LocalDateTime bookingStartTime = bookingRequest.getBookingTime();
        LocalDateTime bookingEndTime = bookingStartTime.plusMinutes(serviceWorkerEntity.getService().getDurationMinutes());

        // Check if the worker has allocated availability for the service
        // TODO - only caters for bookings/services that are within a single day (ie. that don't extend over more than one day)
        List<ServiceWorkerAvailabilityEntity> serviceWorkerAvailabilityEntities = serviceWorkerAvailabilityRepository.getAllByServiceWorkerServiceWorkerIdAndEffectiveStartDateLessThanEqualAndEffectiveEndDateGreaterThanEqual(
                serviceWorkerEntity.getServiceWorkerId(),
                bookingStartTime.toLocalDate(),
                bookingEndTime.toLocalDate()
        );
        DayOfWeek day = bookingStartTime.getDayOfWeek();
        var ref = new Object() {
            boolean valid = false;
        };
        serviceWorkerAvailabilityEntities
                .stream()
                .map(ServiceWorkerAvailabilityEntity::getAvailability)
                .filter(availabilityEntity -> availabilityEntity.getDay().equals(day))
                .takeWhile(availabilityEntity -> !ref.valid)
                .forEach(availability -> {
                    if (!bookingStartTime.toLocalTime().isBefore(availability.getStartTime())
                            && bookingEndTime.toLocalTime().isBefore(availability.getEndTime())) {
                        ref.valid = true;
                    }
                });

        if (!ref.valid) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Requested booking time does not fit within worker's availability schedule");
        }


        // Check if the worker does not have an overlapping booking
        // Inclusive of start, exclusive of end
        List<BookingEntity> serviceBookings = bookingRepository.getAllByServiceWorkerWorkerWorkerIdAndStatus(bookingRequest.getWorkerId(), Status.ACTIVE);
        for (BookingEntity be : serviceBookings) {
            if (!(bookingStartTime.isAfter(be.getBookingTime().plusMinutes(be.getServiceWorker().getService().getDurationMinutes()))
                    || !bookingEndTime.isAfter(be.getBookingTime()))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Worker has an overlapping booking");
            }
        }

        // Check if the customer does not have an overlapping booking
        // Inclusive of start, exclusive of end
        List<BookingEntity> customerBookings = bookingRepository.getAllByCustomerUserUsernameAndStatus(bookingRequest.getCustomerUsername(), Status.ACTIVE);
        for (BookingEntity be : customerBookings) {
            if (!(bookingStartTime.isAfter(be.getBookingTime().plusMinutes(be.getServiceWorker().getService().getDurationMinutes()))
                    || !bookingEndTime.isAfter(be.getBookingTime()))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer has an overlapping booking");
            }
        }

        BookingEntity bookingEntity = new BookingEntity(
                serviceWorkerEntity,
                customerEntity,
                bookingRequest.getBookingTime()
        );

        bookingRepository.save(bookingEntity);
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
