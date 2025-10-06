package tqs.lab3meals.service;

import java.util.*;

import org.springframework.stereotype.Service;
import tqs.lab3meals.data.MealBookingEntity;
import tqs.lab3meals.data.MealBookingRepository;
import tqs.lab3meals.model.*;
@Service
public class MealsBookingService {

    private final MealBookingRepository mealBookingRepository;

    public MealsBookingService(MealBookingRepository mealBookingRepository) {
        this.mealBookingRepository = mealBookingRepository;
    }

    public void cancelReservation(String userId) {
        MealBookingEntity entity = mealBookingRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
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