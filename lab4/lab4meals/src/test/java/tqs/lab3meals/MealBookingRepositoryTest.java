package tqs.lab3meals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import tqs.lab3meals.data.MealBookingEntity;
import tqs.lab3meals.data.MealBookingRepository;
import tqs.lab3meals.model.RESERVATION_STATE;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;


@DataJpaTest
public class MealBookingRepositoryTest {
    @Autowired
    private MealBookingRepository repository;

    @Test
    void testSaveAndFindById() {
        MealBookingEntity booking = new MealBookingEntity();
        booking.setUserId("user1");
        booking.setDate(LocalDate.now());
        booking.setState(RESERVATION_STATE.RESERVADO);

        repository.save(booking);

        MealBookingEntity found = repository.findById("user1").orElse(null);
        assertThat(found).isNotNull();
        Assertions.assertNotNull(found);
        assertThat(found.getState()).isEqualTo(RESERVATION_STATE.RESERVADO);
    }

    @Test
    void testFindByState_CustomQuery() {
        MealBookingEntity booking1 = new MealBookingEntity();
        booking1.setUserId("user1");
        booking1.setDate(LocalDate.now());
        booking1.setState(RESERVATION_STATE.RESERVADO);

        MealBookingEntity booking2 = new MealBookingEntity();
        booking2.setUserId("user2");
        booking2.setDate(LocalDate.now());
        booking2.setState(RESERVATION_STATE.USADO);

        repository.save(booking1);
        repository.save(booking2);

        List<MealBookingEntity> results = repository.findByState(RESERVATION_STATE.RESERVADO);

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getUserId()).isEqualTo("user1");
    }
}
