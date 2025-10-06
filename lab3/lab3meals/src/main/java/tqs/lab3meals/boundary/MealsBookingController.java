package tqs.lab3meals.boundary;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tqs.lab3meals.data.MealBookingEntity;
import tqs.lab3meals.service.MealsBookingService;

import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<?> getBooking(@PathVariable String id) {
        Optional<MealBookingEntity> reservation = service.findReservation(id);
        if (reservation.isPresent()) {
            return ResponseEntity.ok(reservation.get());
        } else {
            return new ResponseEntity<>("Booking not found for id: " + id, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path="/bookings" )
    public List<MealBookingEntity> getAllBookings() {
        return service.getAllReservations();
    }

    @DeleteMapping("/bookings/{id}")
    public ResponseEntity<MealBookingEntity> deleteBookingById(@PathVariable String id)
            throws ResourceNotFoundException {
        MealBookingEntity entity = service.findReservation(id)
                        .orElseThrow(() -> new IllegalArgumentException("Booking not found for id: " + id));
        service.cancelReservation(id);
        return ResponseEntity.ok(entity);
    }
    @PutMapping("/bookings/{id}/checkin")
    public ResponseEntity<String> checkInBooking(@PathVariable String id) {
        service.checkIn(id);
        return ResponseEntity.ok("Booking checked in successfully");
    }
}
