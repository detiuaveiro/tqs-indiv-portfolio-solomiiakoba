package tqs.ex2.service;

import java.util.*;

import org.springframework.stereotype.Service;
import tqs.ex2.data.MealBookingEntity;
import tqs.ex2.data.MealBookingRepository;
import tqs.ex2.model.RESERVATION_STATE;
@Service
public class MealsBookingService {

    private final MealBookingRepository mealBookingRepository;

    public MealsBookingService(MealBookingRepository mealBookingRepository) {
        this.mealBookingRepository = mealBookingRepository;
    }

    public void cancelReservation(String userId) {
        MealBookingEntity entity = mealBookingRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

        if (entity.getState() != RESERVATION_STATE.RESERVADO) {
            throw new IllegalStateException("Cannot cancel reservation that is not in RESERVADO state");
        }

        entity.setState(RESERVATION_STATE.CANCELADO);
        mealBookingRepository.save(entity);
    }


    public Optional<MealBookingEntity> findReservation(String userId) {
        return mealBookingRepository.findById(userId);
    }

    public void checkIn(String userId) {
        MealBookingEntity entity = mealBookingRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
        if (entity.getState() != RESERVATION_STATE.RESERVADO) {
            throw new IllegalArgumentException("The reservation was already used or cancelled");
        }
        entity.setState(RESERVATION_STATE.USADO);
        mealBookingRepository.save(entity);
    }

    public String bookMeal(MealBookingEntity entity) {
        if (mealBookingRepository.existsById(entity.getUserId())) {
            throw new IllegalArgumentException("Reservation already exists for this user");
        }

        entity.setState(RESERVATION_STATE.RESERVADO);
        mealBookingRepository.save(entity);
        return entity.getUserId();
    }

    public List<MealBookingEntity> getAllReservations() {
        return mealBookingRepository.findAll();
    }
}