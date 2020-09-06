package com.rmit.sept.septbackend.controller;

import com.rmit.sept.septbackend.model.BookingRequest;
import com.rmit.sept.septbackend.service.BookingService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/booking")
@CrossOrigin(origins = "*", maxAge = 3600)
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/create")
    public void createBooking(@Valid @RequestBody BookingRequest bookingRequest) {
        bookingService.createBooking(bookingRequest);
    }
}
