package com.rmit.sept.septbackend.service;

import com.rmit.sept.septbackend.entity.*;
import com.rmit.sept.septbackend.model.BookingRequest;
import com.rmit.sept.septbackend.model.BookingResponse;
import com.rmit.sept.septbackend.model.Status;
import com.rmit.sept.septbackend.model.ValidationResponse;
import com.rmit.sept.septbackend.repository.BookingRepository;
import com.rmit.sept.septbackend.repository.CustomerRepository;
import com.rmit.sept.septbackend.repository.ServiceWorkerAvailabilityRepository;
import com.rmit.sept.septbackend.repository.ServiceWorkerRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
                    bookingEntity.getBookingId(),
                    username
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
                    bookingEntity.getBookingId(),
                    bookingEntity.getCustomer().getUser().getUsername()
            );
        }).collect(Collectors.toList()
        );
    }

    public ValidationResponse<Void> createBooking(BookingRequest bookingRequest) {
        ValidationResponse<Void> validationResponse = new ValidationResponse<>();

        // Validation performed (in order):
        //     - Check if the customer exists
        //     - Check if the service/worker relationship exists
        //     - Check if the worker has allocated availability for the service
        //     - Check if the worker does not have an overlapping booking
        //     - Check if the customer does not have an overlapping booking

        // Check if the customer exists
        CustomerEntity customerEntity = customerRepository.getByUserUsername(bookingRequest.getCustomerUsername());
        if (customerEntity == null) {
            validationResponse.addError("Customer doesn't exist", "Customer doesn't exist [username=%s]", bookingRequest.getCustomerUsername());
            // Guard statement - customerEntity is required below
            return validationResponse;
        }

        // Check if the service/worker relationship exists
        ServiceWorkerEntity serviceWorkerEntity = serviceWorkerRepository.getByServiceServiceIdAndWorkerWorkerId(bookingRequest.getServiceId(), bookingRequest.getWorkerId());
        if (serviceWorkerEntity == null) {
            validationResponse.addError("Service/worker relationship doesn't exist",
                    "Service/worker relationship doesn't exist [serviceId=%d,workerId=%d]",
                    bookingRequest.getServiceId(),
                    bookingRequest.getWorkerId()
            );
            // Guard statement - serviceWorkerEntity is required below
            return validationResponse;
        }

        LocalDateTime bookingStartTime = bookingRequest.getBookingTime();
        LocalDateTime bookingEndTime = bookingStartTime.plusMinutes(serviceWorkerEntity.getService().getDurationMinutes());

        // Check if the worker has allocated availability for the service
        // TODO - only caters for bookings/services that are within a single day (ie. that don't extend over more than one day)
        List<ServiceWorkerAvailabilityEntity> serviceWorkerAvailabilityEntities = serviceWorkerAvailabilityRepository.getAllByServiceWorkerId(
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
            validationResponse.addError("Requested booking time does not fit within worker's availability schedule");
        }


        // Check if the worker does not have an overlapping booking
        // Inclusive of start, exclusive of end
        List<BookingEntity> serviceBookings = bookingRepository.getAllByServiceWorkerWorkerWorkerIdAndStatus(bookingRequest.getWorkerId(), Status.ACTIVE);
        for (BookingEntity be : serviceBookings) {
            if (!(bookingStartTime.isAfter(be.getBookingTime().plusMinutes(be.getServiceWorker().getService().getDurationMinutes()))
                    || !bookingEndTime.isAfter(be.getBookingTime()))) {
                validationResponse.addError(
                        "Worker has an overlapping booking",
                        "Worker has an overlapping booking [newBookingTime=%s,existingBookingTime=%s,serviceDurationMinutes=%d]",
                        bookingStartTime,
                        be.getBookingTime(),
                        be.getServiceWorker().getService().getDurationMinutes()
                );
            }
        }

        // Check if the customer does not have an overlapping booking
        // Inclusive of start, exclusive of end
        List<BookingEntity> customerBookings = bookingRepository.getAllByCustomerUserUsernameAndStatus(bookingRequest.getCustomerUsername(), Status.ACTIVE);
        for (BookingEntity be : customerBookings) {
            if (!(bookingStartTime.isAfter(be.getBookingTime().plusMinutes(be.getServiceWorker().getService().getDurationMinutes()))
                    || !bookingEndTime.isAfter(be.getBookingTime()))) {
                validationResponse.addError(
                        "Customer has an overlapping booking",
                        "Customer has an overlapping booking [newBookingTime=%s,existingBookingTime=%s,serviceDurationMinutes=%d]",
                        bookingStartTime,
                        be.getBookingTime(),
                        be.getServiceWorker().getService().getDurationMinutes()
                );

            }
        }

        BookingEntity bookingEntity = new BookingEntity(
                serviceWorkerEntity,
                customerEntity,
                bookingRequest.getBookingTime()
        );

        bookingRepository.save(bookingEntity);

        return validationResponse;
    }

    public ValidationResponse<Void> cancelBooking(int bookingId) {
        ValidationResponse<Void> validationResponse = new ValidationResponse<>();
        Optional<BookingEntity> entity = bookingRepository.findById(bookingId);
        if (entity.isPresent()) {
            BookingEntity bookingEntity = entity.get();
            bookingEntity.setStatus(Status.CANCELLED);
            bookingRepository.save(bookingEntity);
        } else {
            validationResponse.addError(
                    "There was an error cancelling your booking",
                    "Booking not found [bookingId=%d]",
                    bookingId
            );
        }

        return validationResponse;
    }
}
