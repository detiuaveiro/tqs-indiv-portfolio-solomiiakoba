package tqs;

import java.util.*;

public class MealsBookingService {
    private HashMap <String, MealBookingRequest> reservations;

    public MealsBookingService() {
        this.reservations = new HashMap<>();
    }
    public void cancelReservation(String userId) {
        MealBookingRequest request = reservations.get(userId);
        if (request == null) {
            throw new IllegalArgumentException("Reservation not found");
        }
        if (request.getState() != RESERVATION_STATE.RESERVADO) {
            throw new IllegalStateException("The reservation was either cancelled or doesn't exist");
        }
        request.setState(RESERVATION_STATE.CANCELADO);
        reservations.remove(userId);
    }
    public MealBookingRequest findReservation(String userId) {
        return reservations.getOrDefault(userId, null);
    }
    public void checkIn(String userId) {
        MealBookingRequest request = reservations.get(userId);
        if (request == null) {
            throw new IllegalArgumentException("The reservation wasn't found");
        }
        if (request.getState() != RESERVATION_STATE.RESERVADO) {
            throw new IllegalArgumentException("The reservation was already used or cancelled by the user");
        }
        request.setState(RESERVATION_STATE.USADO);
    }
    public String bookMeal(MealBookingRequest request) {
        String userId = request.getUserId();
        if  (!reservations.containsKey(userId)) {
            reservations.put(userId, request);
            request.setState(RESERVATION_STATE.RESERVADO);
        } else {
            throw new IllegalArgumentException("The reservation was already done");
        }
        return userId;
    }
}