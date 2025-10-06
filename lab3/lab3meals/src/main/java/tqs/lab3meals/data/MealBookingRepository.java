package tqs.lab3meals.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MealBookingRepository extends JpaRepository<MealBookingEntity, String> {
}
