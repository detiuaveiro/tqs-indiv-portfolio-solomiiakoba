package tqs.ex2.boundary;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tqs.ex2.data.MealBookingEntity;
import tqs.ex2.service.MealsBookingService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MealsBookingController {
    private final MealsBookingService service;

    public MealsBookingController(MealsBookingService service) {
        this.service = service;
    }

    @PostMapping("/bookings")
    public ResponseEntity<String> createBooking(@RequestBody MealBookingEntity booking) {
        String id = service.bookMeal(booking);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @GetMapping("/bookings/{id}")
    public ResponseEntity<MealBookingEntity> getBooking(@PathVariable String id) {
        MealBookingEntity reservation = service.findReservation(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Booking not found for id: " + id));
        return ResponseEntity.ok(reservation);
    }


    @GetMapping(path="/bookings" )
    public List<MealBookingEntity> getAllBookings() {
        return service.getAllReservations();
    }

    @DeleteMapping("/bookings/{id}")
    public ResponseEntity<MealBookingEntity> deleteBookingById(@PathVariable String id) {
        MealBookingEntity entity = service.findReservation(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Booking not found for id: " + id));
        service.cancelReservation(id);
        return ResponseEntity.ok(entity);
    }

    @PutMapping("/bookings/{id}/checkin")
    public ResponseEntity<String> checkInBooking(@PathVariable String id) {
        service.checkIn(id);
        return ResponseEntity.ok("Booking checked in successfully");
    }
}
