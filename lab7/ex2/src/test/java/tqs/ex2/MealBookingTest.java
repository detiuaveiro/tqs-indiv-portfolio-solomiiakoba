package tqs.ex2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tqs.ex2.data.MealBookingEntity;
import tqs.ex2.data.MealBookingRepository;
import tqs.ex2.model.RESERVATION_STATE;
import tqs.ex2.service.MealsBookingService;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MealBookingTest {

    private MealBookingRepository repository;
    private MealsBookingService service;

    @BeforeEach
    void setUp() {
        repository = mock(MealBookingRepository.class);
        service = new MealsBookingService(repository);
    }

    @Test
    @DisplayName("Cancellation of existing reservation")
    void testCancellingExistingReservation() {
        MealBookingEntity entity = new MealBookingEntity("student123", LocalDate.of(2025, 10, 10), RESERVATION_STATE.RESERVADO);
        when(repository.findById("student123")).thenReturn(Optional.of(entity));

        service.cancelReservation("student123");

        assertEquals(RESERVATION_STATE.CANCELADO, entity.getState());
        verify(repository).save(entity);
    }

    @Test
    @DisplayName("Cancel non-existing reservation")
    void testCancellingNonExistingReservation() {
        when(repository.findById("student1")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.cancelReservation("student1"));
    }

    @Test
    @DisplayName("Cancel a reservation with invalid state")
    void testCancelingInvalidReservation() {
        MealBookingEntity entity = new MealBookingEntity("student111", LocalDate.of(2025, 10, 10), RESERVATION_STATE.USADO);
        when(repository.findById("student111")).thenReturn(Optional.of(entity));

        assertThrows(IllegalStateException.class, () -> service.cancelReservation("student111"));
    }

    @Test
    @DisplayName("Booking the same student twice throws exception")
    void testBookMealDuplicate() {
        // Reserva existente
        when(repository.existsById("student1")).thenReturn(true);

        MealBookingEntity request2 = new MealBookingEntity("student1", LocalDate.of(2025, 10, 11), RESERVATION_STATE.RESERVADO);

        assertThrows(IllegalArgumentException.class, () -> service.bookMeal(request2));
    }

    @Test
    @DisplayName("Finding existing reservation")
    void testFindExistingReservation() {
        MealBookingEntity entity = new MealBookingEntity("student123", LocalDate.of(2025, 10, 10), RESERVATION_STATE.RESERVADO);
        when(repository.findById("student123")).thenReturn(Optional.of(entity));
        when(repository.findById("student1234567")).thenReturn(Optional.empty());

        Optional<MealBookingEntity> res1 = service.findReservation("student123");
        assertTrue(res1.isPresent());

        Optional<MealBookingEntity> res2 = service.findReservation("student1234567");
        assertTrue(res2.isEmpty());
    }

    @Test
    @DisplayName("Check-in a valid reservation")
    void testCheckInValidReservation() {
        MealBookingEntity entity = new MealBookingEntity("student1", LocalDate.of(2025, 10, 10), RESERVATION_STATE.RESERVADO);
        when(repository.findById("student1")).thenReturn(Optional.of(entity));

        service.checkIn("student1");

        assertEquals(RESERVATION_STATE.USADO, entity.getState());
        verify(repository).save(entity);
    }

    @Test
    @DisplayName("Check-in a non-existing reservation throws exception")
    void testCheckInNonExisting() {
        when(repository.findById("studentX")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.checkIn("studentX"));
    }

    @Test
    @DisplayName("Check-in a cancelled reservation throws exception")
    void testCheckInCancelled() {
        MealBookingEntity entity = new MealBookingEntity("student2223", LocalDate.of(2025, 10, 10), RESERVATION_STATE.CANCELADO);
        when(repository.findById("student2223")).thenReturn(Optional.of(entity));

        assertThrows(IllegalArgumentException.class, () -> service.checkIn("student2223"));
    }
}