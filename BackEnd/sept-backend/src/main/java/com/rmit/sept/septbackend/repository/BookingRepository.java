package com.rmit.sept.septbackend.repository;

import com.rmit.sept.septbackend.entity.BookingEntity;
import com.rmit.sept.septbackend.entity.BusinessEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends CrudRepository<BookingEntity, Integer> {
    BookingEntity getByBookingId(String bookingId);
}
