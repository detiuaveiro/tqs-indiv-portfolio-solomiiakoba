package com.tqs.zeromonos.controller;

import com.tqs.zeromonos.model.BookingRequestDTO;
import com.tqs.zeromonos.model.BookingResponseDTO;
import com.tqs.zeromonos.model.StatusUpdateDTO;
import com.tqs.zeromonos.service.BookingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/bookings")
@Slf4j
public class BookingController {
    private final BookingService bookingService;
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }
    @PostMapping
    public ResponseEntity<BookingResponseDTO> createBooking(@RequestBody BookingRequestDTO request) {
        log.info("POST /api/bookings - Creating new booking.");
        BookingResponseDTO response = bookingService.createBooking(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping("/{token}")
    public ResponseEntity<BookingResponseDTO> getBooking(@PathVariable String token) {
        log.info("GET /api/bookings/{} - Getting booking.", token);
        BookingResponseDTO response = bookingService.getBookingByToken(token);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<List<BookingResponseDTO>> getAllBookings(@RequestParam(required = false) String municipality) {
        log.info("GET /api/bookings - Getting all bookings for municipality");
        List<BookingResponseDTO> bookings = municipality != null
                ? bookingService.getBookingsByMunicipality(municipality)
                : bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }
    @PatchMapping("/{token}/status")
    public ResponseEntity<BookingResponseDTO> updateBookingStatus(@PathVariable String token, @RequestBody StatusUpdateDTO status) {
        log.info("PATCH /api/bookings/{}/status - Updating booking status.", token);
        BookingResponseDTO response = bookingService.updateBookingStatus(token, status);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{token}")
    public ResponseEntity<BookingResponseDTO> cancelBooking(@PathVariable String token) {
        log.info("DELETE /api/bookings/{} - Deleting booking.", token);
        bookingService.cancelBooking(token);
        return ResponseEntity.noContent().build();
    }
}
