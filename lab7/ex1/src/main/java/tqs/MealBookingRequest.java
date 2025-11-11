package tqs;

import java.time.LocalDate;

public class MealBookingRequest {
    LocalDate date;
    RESERVATION_STATE state;
    String userId;

    public MealBookingRequest(String userId, LocalDate date, RESERVATION_STATE state) {
        this.date = date;
        this.state = state;
        this.userId = userId;
    }
    // Getters
    public RESERVATION_STATE getState() { return state; }
    public String getUserId() { return userId; }
    public void setState(RESERVATION_STATE state) { this.state = state; }
}
