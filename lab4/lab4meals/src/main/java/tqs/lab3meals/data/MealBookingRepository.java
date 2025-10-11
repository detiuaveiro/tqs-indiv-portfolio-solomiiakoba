package tqs.lab3meals.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tqs.lab3meals.model.RESERVATION_STATE;

import java.util.List;
@Repository

public interface MealBookingRepository extends JpaRepository<MealBookingEntity, String> {
    // encontrar reservas por estado
    @Query("SELECT m FROM MealBookingEntity m WHERE m.state = :state")
    List<MealBookingEntity> findByState(RESERVATION_STATE state);
}
