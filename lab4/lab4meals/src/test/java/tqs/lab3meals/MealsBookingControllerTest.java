package tqs.lab3meals;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tqs.lab3meals.boundary.MealsBookingController;
import tqs.lab3meals.data.MealBookingEntity;
import tqs.lab3meals.model.RESERVATION_STATE;
import tqs.lab3meals.service.MealsBookingService;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

@WebMvcTest(MealsBookingController.class)
public class MealsBookingControllerTest {
    @Autowired
    private MockMvc mockMvc;    // entry point for http requests

    @Autowired
    private ObjectMapper objectMapper; // objects java -> json

    @MockBean
    private MealsBookingService service;

    @Test
    void createBooking () throws Exception{
        MealBookingEntity mealBookingEntity = new MealBookingEntity();
        mealBookingEntity.setUserId("user1");
        mealBookingEntity.setDate(LocalDate.now());
        when(service.bookMeal(any(MealBookingEntity.class))).thenReturn("user1");
        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mealBookingEntity)))
                .andExpect(status().isCreated())
                .andExpect(content().string("user1"));


        verify(service, times(1)).bookMeal(Mockito.any());  // verify call times
    }
    @Test
    void getBooking_found() throws Exception{
        MealBookingEntity mealBookingEntity = new MealBookingEntity();
        mealBookingEntity.setUserId("user2");
        mealBookingEntity.setDate(LocalDate.now());
        mealBookingEntity.setState(RESERVATION_STATE.RESERVADO);

        when(service.findReservation("user2")).thenReturn(Optional.of(mealBookingEntity));
        mockMvc.perform(get("/api/bookings/user2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is("user2")))
                .andExpect(jsonPath("$.state", is(RESERVATION_STATE.RESERVADO.toString())));
    }
    @Test
    void getBooking_not_found() throws Exception{
        when(service.findReservation("no user")).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/bookings/user2"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Booking not found")));
    }
    @Test
    void getAllBookings_list() throws Exception{
        MealBookingEntity e1 = new MealBookingEntity();
        e1.setUserId("user1");
        e1.setDate(LocalDate.now());
        MealBookingEntity e2 = new MealBookingEntity();
        e2.setUserId("user2");
        when(service.getAllReservations()).thenReturn(Arrays.asList(e1,e2));
        mockMvc.perform(get("/api/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value("user1"))
                .andExpect(jsonPath("$[1].userId").value("user2"));

        verify(service).getAllReservations();
    }
    @Test
    void deleteBooking_found() throws Exception{
        MealBookingEntity e = new MealBookingEntity();
        e.setUserId("user3");
        e.setDate(LocalDate.now());
        when(service.findReservation("user3")).thenReturn(Optional.of(e));
        mockMvc.perform(delete("/api/bookings/user3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("user3"));

        verify(service).cancelReservation("user3");
    }
    @Test
    void checkBooking_found() throws Exception{
        MealBookingEntity e = new MealBookingEntity();
        e.setUserId("user4");
        e.setDate(LocalDate.now());
        e.setState(RESERVATION_STATE.RESERVADO);
        when(service.findReservation("user4")).thenReturn(Optional.of(e));
        mockMvc.perform(put("/api/bookings/user4/checkin"))
                .andExpect(status().isOk())
                .andExpect(content().string("Booking checked in successfully"));
        verify(service, times(1)).checkIn("user4");
    }
}
