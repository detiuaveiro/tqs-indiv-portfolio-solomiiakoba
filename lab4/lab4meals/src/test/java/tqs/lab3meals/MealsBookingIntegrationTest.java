package tqs.lab3meals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import tqs.lab3meals.boundary.MealsBookingController;
import tqs.lab3meals.data.MealBookingEntity;
import tqs.lab3meals.data.MealBookingRepository;
import tqs.lab3meals.model.RESERVATION_STATE;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource("classpath:application-integrationtest.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MealsBookingIntegrationTest {

    @Autowired
    private MealsBookingController controller;

    @Autowired
    private MealBookingRepository repository;

    @Test
    void testCreateAndRetrieveBooking() {
        MealBookingEntity booking = new MealBookingEntity();
        booking.setUserId("user1");
        booking.setDate(LocalDate.now());
        booking.setState(RESERVATION_STATE.RESERVADO);

        controller.createBooking(booking);

        // verifica que foi salvo no repositório
        MealBookingEntity saved = repository.findById("user1").orElse(null);
        assertThat(saved).isNotNull();
        assertThat(saved.getUserId()).isEqualTo("user1");

        List<MealBookingEntity> reserved = repository.findAll();
        assertThat(reserved).hasSize(1);
    }

    @Test
    void testCheckInBooking() {
        MealBookingEntity booking = new MealBookingEntity();
        booking.setUserId("user2");
        booking.setDate(LocalDate.now());
        booking.setState(RESERVATION_STATE.RESERVADO);

        controller.createBooking(booking);
        controller.checkInBooking("user2");

        MealBookingEntity updated = repository.findById("user2").orElse(null);
        Assertions.assertNotNull(updated);
        assertThat(updated.getState()).isEqualTo(RESERVATION_STATE.USADO);
    }
}
