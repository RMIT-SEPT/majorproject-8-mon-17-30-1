package com.rmit.sept.septbackend.controller;

import com.rmit.sept.septbackend.model.BookingRequest;
import com.rmit.sept.septbackend.model.BookingResponse;
import com.rmit.sept.septbackend.model.Status;
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

    @GetMapping("/viewActive")
    public List<BookingResponse> viewBookings(@Valid @RequestParam(name = "username") String username) {
        return bookingService.viewBookings(username, Status.ACTIVE);
    }

    @GetMapping("/viewHistory")
    public List<BookingResponse> viewBookingHistory(@Valid @RequestParam(name = "username") String username) {
        return bookingService.viewBookings(username, Status.CANCELLED);
    }

    @GetMapping("/viewAllHistory")
    public List<BookingResponse> viewAllBookingHistory() {
        return bookingService.viewAllPastBookings();
    }

    @PostMapping("/create")
    public void createBooking(@Valid @RequestBody BookingRequest bookingRequest) {
        bookingService.createBooking(bookingRequest);
    }

    @DeleteMapping("/cancel/{bookingId}")
    public void cancelBooking(@Valid @PathVariable int bookingId) {
        bookingService.cancelBooking(bookingId);
    }
}
