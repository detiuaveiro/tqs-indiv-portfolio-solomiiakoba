package tqs.ex2;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tqs.ex2.boundary.MealsBookingController;
import tqs.ex2.data.MealBookingEntity;
import tqs.ex2.model.RESERVATION_STATE;
import tqs.ex2.service.MealsBookingService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MealsBookingController.class)
class MealsBookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
    @MockBean
    private MealsBookingService service;

    private final ObjectMapper mapper = new ObjectMapper()
            .findAndRegisterModules();

    @Test
    @DisplayName("POST /bookings - create booking")
    void testCreateBooking() throws Exception {
        MealBookingEntity booking = new MealBookingEntity("student1", LocalDate.of(2025, 10, 10), RESERVATION_STATE.RESERVADO);
        Mockito.when(service.bookMeal(any(MealBookingEntity.class))).thenReturn("student1");

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(booking)))
                .andExpect(status().isCreated())
                .andExpect(content().string("student1"));
    }

    @Test
    @DisplayName("GET /bookings/{id} - existing booking")
    void testGetBookingExisting() throws Exception {
        MealBookingEntity entity = new MealBookingEntity("student1", LocalDate.of(2025, 10, 10), RESERVATION_STATE.RESERVADO);
        Mockito.when(service.findReservation("student1")).thenReturn(Optional.of(entity));

        mockMvc.perform(get("/api/bookings/student1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("student1"))
                .andExpect(jsonPath("$.state").value("RESERVADO"));
    }

    @Test
    @DisplayName("GET /bookings/{id} - non-existing booking")
    void testGetBookingNotFound() throws Exception {
        Mockito.when(service.findReservation("studentX")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/bookings/studentX"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /bookings - all bookings")
    void testGetAllBookings() throws Exception {
        MealBookingEntity e1 = new MealBookingEntity("student1", LocalDate.of(2025, 10, 10), RESERVATION_STATE.RESERVADO);
        MealBookingEntity e2 = new MealBookingEntity("student2", LocalDate.of(2025, 10, 11), RESERVATION_STATE.RESERVADO);
        Mockito.when(service.getAllReservations()).thenReturn(Arrays.asList(e1, e2));

        mockMvc.perform(get("/api/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].userId").value("student1"))
                .andExpect(jsonPath("$[1].userId").value("student2"));
    }

    @Test
    @DisplayName("DELETE /bookings/{id} - cancel existing booking")
    void testDeleteBooking() throws Exception {
        MealBookingEntity entity = new MealBookingEntity("student1", LocalDate.of(2025, 10, 10), RESERVATION_STATE.RESERVADO);
        Mockito.when(service.findReservation("student1")).thenReturn(Optional.of(entity));

        mockMvc.perform(delete("/api/bookings/student1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("student1"));

        Mockito.verify(service).cancelReservation("student1");
    }

    @Test
    @DisplayName("DELETE /bookings/{id} - non-existing booking")
    void testDeleteBookingNotFound() throws Exception {
        Mockito.when(service.findReservation("studentX")).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/bookings/studentX"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /bookings/{id}/checkin - check-in booking")
    void testCheckInBooking() throws Exception {
        mockMvc.perform(put("/api/bookings/student1/checkin"))
                .andExpect(status().isOk())
                .andExpect(content().string("Booking checked in successfully"));

        Mockito.verify(service).checkIn("student1");
    }
}
