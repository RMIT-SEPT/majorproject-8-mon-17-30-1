package com.rmit.sept.septbackend.repository;

import com.rmit.sept.septbackend.entity.BookingEntity;
import com.rmit.sept.septbackend.model.Status;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
    public interface BookingRepository extends CrudRepository<BookingEntity, Integer> {
    List<BookingEntity> getAllByCustomerUserUsernameAndStatus(String username, Status status);
    BookingEntity getByBookingId(String bookingId);
}
