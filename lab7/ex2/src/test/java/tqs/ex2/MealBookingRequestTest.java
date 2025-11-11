package tqs.ex2;

import org.junit.jupiter.api.Test;
import tqs.ex2.model.MealBookingRequest;
import tqs.ex2.model.RESERVATION_STATE;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
class MealBookingRequestTest {

    @Test
    void testGettersAndSetters() {
        MealBookingRequest req = new MealBookingRequest();
        req.setUserId("student123");
        req.setDate(LocalDate.of(2025, 10, 10));
        req.setState(RESERVATION_STATE.RESERVADO);

        assertEquals("student123", req.getUserId());
        assertEquals(LocalDate.of(2025, 10, 10), req.getDate());
        assertEquals(RESERVATION_STATE.RESERVADO, req.getState());
    }

    @Test
    void testConstructorAndToString() {
        MealBookingRequest req = new MealBookingRequest("student1", LocalDate.of(2025, 11, 10), RESERVATION_STATE.RESERVADO);
        assertNotNull(req.toString());
    }
}
