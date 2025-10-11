package tqs.lab3meals.model;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public class MealBookingRequest {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate date;
    RESERVATION_STATE state;
    String userId;

    public MealBookingRequest() {
    }

    public MealBookingRequest(String userId, LocalDate date, RESERVATION_STATE state) {
        this.date = date;
        this.state = state;
        this.userId = userId;
    }

    public LocalDate getDate() { return date; }
    public RESERVATION_STATE getState() { return state; }
    public String getUserId() { return userId; }

    public void setDate(LocalDate date) { this.date = date; }
    public void setState(RESERVATION_STATE state) { this.state = state; }
    public void setUserId(String userId) { this.userId = userId; }
}