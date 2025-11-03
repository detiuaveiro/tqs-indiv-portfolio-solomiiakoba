package com.tqs.zeromonos;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tqs.zeromonos.model.BookingRequestDTO;
import com.tqs.zeromonos.model.ServiceRequestRepository;
import com.tqs.zeromonos.model.TimeSlot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.DayOfWeek;
import java.time.LocalDate;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BookingIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ServiceRequestRepository repository;

    @BeforeEach
    void setup() {repository.deleteAll();}

    @Test
    void testCreatingValidBooking() throws Exception {
        BookingRequestDTO request = new BookingRequestDTO();
        request.setDescription("Old sofa and dryer...");
        request.setMunicipality("Aveiro");
        request.setCollectionDate(getNextWeekday());
        request.setTimeSlot(TimeSlot.MORNING);

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.municipality").value("Aveiro"))
                .andExpect(jsonPath("$.currentState").value("RECEIVED"))
                .andExpect(jsonPath("$.statusHistory").isArray())
                .andExpect(jsonPath("$.statusHistory", hasSize(greaterThan(0))));
    }
    @Test
    void testCreatingInvalidBookingWithInvalidDate() throws Exception {
        BookingRequestDTO request = new BookingRequestDTO();
        request.setDescription("nada");
        request.setMunicipality("Aveiro");
        request.setCollectionDate(LocalDate.now());

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Invalid date range")));
    }

    @Test
    void testGettingByToken_Status200() throws Exception {
        BookingRequestDTO request = new BookingRequestDTO();
        request.setDescription("Old sofa and dryer...");
        request.setMunicipality("Aveiro");
        request.setCollectionDate(getNextWeekday());
        request.setTimeSlot(TimeSlot.MORNING);
        MvcResult result = mockMvc.perform(post("/api/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated()).andReturn();
        String response = result.getResponse().getContentAsString();
        String token = objectMapper.readTree(response).get("token").asText();
        mockMvc.perform(get("/api/bookings/" + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(token))
                .andExpect(jsonPath("$.municipality").value("Aveiro"));
    }
    @Test
    void gettingBookingByInvalidToken() throws Exception {
        mockMvc.perform(get("/api/bookings/invalid"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }
    @Test
    void testGettingAllBookings() throws Exception {
        createTestBooking("Aveiro", TimeSlot.MORNING);
        createTestBooking("Aveiro", TimeSlot.AFTERNOON);
        mockMvc.perform(get("/api/bookings?municipality=Aveiro"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].municipality").value("Aveiro"))
                .andExpect(jsonPath("$[1].municipality").value("Aveiro"));
    }
    @Test
    void updateBookingStatusReturnsOk() throws Exception {
        String token = createTestBooking("Aveiro", TimeSlot.MORNING);
        mockMvc.perform(delete("/api/bookings/" + token)).andExpect(status().isNoContent());
        mockMvc.perform(get("/api/bookings/" + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentState").value("CANCELED"));
    }
    @Test
    void whenExceedSlotLimit_thenStatus409() throws Exception {
        LocalDate targetDate = getNextWeekday();
        TimeSlot[] slots = TimeSlot.values();

        // Cria 63 bookings
        for (int i = 0; i < 63; i++) {
            BookingRequestDTO req = new BookingRequestDTO();
            req.setDescription("Booking number " + i);
            req.setMunicipality("Aveiro");
            req.setCollectionDate(targetDate);
            req.setTimeSlot(slots[i % slots.length]);

            mockMvc.perform(post("/api/bookings")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(req)))
                    .andExpect(status().isCreated());
        }
        // 64+ booking falha, qualquer slot já cheio
        BookingRequestDTO request = new BookingRequestDTO();
        request.setDescription("Exceed slot limit");
        request.setMunicipality("Aveiro");
        request.setCollectionDate(targetDate);
        request.setTimeSlot(TimeSlot.MORNING); // slot já cheio

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(containsString("limit")));
    }

    private String createTestBooking(String municipality, TimeSlot timeSlot) throws Exception {
        BookingRequestDTO request = new BookingRequestDTO();
        request.setDescription("Test booking for " + municipality);
        request.setMunicipality(municipality);
        request.setCollectionDate(getNextWeekday());
        request.setTimeSlot(timeSlot);

        MvcResult result = mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isCreated())
                        .andReturn();
        String response = result.getResponse().getContentAsString();
        return objectMapper.readTree(response).get("token").asText();
    }
    private LocalDate getNextWeekday() {
        LocalDate date = LocalDate.now().plusDays(2);
        while (date.getDayOfWeek() == DayOfWeek.SATURDAY ||
                date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            date = date.plusDays(1);
        }
        return date;
    }
}
