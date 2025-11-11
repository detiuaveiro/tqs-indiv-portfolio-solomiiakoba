package tqs;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;


class MealBookingTest {
    @Test
    @DisplayName("Cancellation existing reservation")
    void testCancellingExistingReservation() {
        MealsBookingService mealsBookingService = new MealsBookingService();
        MealBookingRequest request = new MealBookingRequest("student123", LocalDate.of(2025, 10, 10), RESERVATION_STATE.RESERVADO);
        mealsBookingService.bookMeal(request);
        mealsBookingService.cancelReservation(request.getUserId());
        MealBookingRequest res =  mealsBookingService.findReservation(request.getUserId());
        assertNull(res, "Reservation should be null after cancellation");
    }
    @Test
    @DisplayName("Cancel non-existing reservation")
    void testCancellingNonExistingReservation() {
        MealsBookingService service = new MealsBookingService();
        assertThrows(IllegalArgumentException.class, () -> service.cancelReservation("student1"));
    }
    @Test
    @DisplayName("Cancel a reservation with invalid state")
    void testCancelingInvalidReservation() {
        MealsBookingService service = new MealsBookingService();
        MealBookingRequest req = new MealBookingRequest("student111", LocalDate.of(2025, 10, 10), RESERVATION_STATE.RESERVADO);
        service.bookMeal(req);
        req.setState(RESERVATION_STATE.USADO);
        assertThrows(IllegalStateException.class, () -> service.cancelReservation("student111"));
    }
    @Test
    @DisplayName("Booking the same student twice throws exception")
    void testBookMealDuplicate() {
        MealsBookingService service = new MealsBookingService();
        MealBookingRequest request = new MealBookingRequest(
                "student1", LocalDate.of(2025, 10, 10), RESERVATION_STATE.RESERVADO
        );
        service.bookMeal(request);
        MealBookingRequest request2 = new MealBookingRequest(
                "student1", LocalDate.of(2025, 10, 11), RESERVATION_STATE.RESERVADO
        );
        assertThrows(IllegalArgumentException.class, () -> service.bookMeal(request2));
    }
    @Test
    @DisplayName("Finding existing reservation")
    void testFindExistingReservation() {
        MealsBookingService service = new MealsBookingService();
        MealBookingRequest request = new MealBookingRequest("student123", LocalDate.of(2025, 10, 10), RESERVATION_STATE.RESERVADO);
        service.bookMeal(request);
        assertNotNull(service.findReservation("student123"));
        assertNull(service.findReservation("student1234567"));
    }
    @Test
    @DisplayName("Check-in a valid reservation")
    void testCheckInValidReservation() {
        MealsBookingService service = new MealsBookingService();
        MealBookingRequest request = new MealBookingRequest("student1", LocalDate.of(2025, 10, 10), RESERVATION_STATE.RESERVADO);
        service.bookMeal(request);
        service.checkIn("student1");
        assertEquals(RESERVATION_STATE.USADO, request.getState(), "Reservation state should be USADO");
    }
    @Test
    @DisplayName("Check-in a non-existing reservation throws exception")
    void testCheckInNonExisting() {
        MealsBookingService service = new MealsBookingService();
        assertThrows(IllegalArgumentException.class, () -> service.checkIn("studentX"));
    }
    @Test
    @DisplayName("Check-in a cancelled reservation throws exception")
    void testCheckInCancelled() {
        MealsBookingService service = new MealsBookingService();
        MealBookingRequest req = new MealBookingRequest("student2223", LocalDate.of(2025, 10, 10), RESERVATION_STATE.RESERVADO);
        service.bookMeal(req);
        req.setState(RESERVATION_STATE.CANCELADO);
        assertThrows(IllegalArgumentException.class, () -> service.checkIn("student2223"));
    }
}
