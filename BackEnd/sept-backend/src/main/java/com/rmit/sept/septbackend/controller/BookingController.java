package com.rmit.sept.septbackend.controller;

import com.rmit.sept.septbackend.model.BookingRequest;
import com.rmit.sept.septbackend.model.BookingResponse;
import com.rmit.sept.septbackend.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/booking")
@CrossOrigin(origins = "*", maxAge = 3600)
public class BookingController {
    @Autowired
    private BookingService bookingService;
    @GetMapping
    public List<BookingResponse> viewBookings(@Valid @RequestParam(name = "username") String username) {

        return bookingService.viewBookings(username);
    }

    @PostMapping("/create")
    public void createBooking(@Valid @RequestBody BookingRequest bookingRequest) {
        bookingService.createBooking(bookingRequest);
    }
}
