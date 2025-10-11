package tqs.lab3meals.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDate;
import tqs.lab3meals.model.RESERVATION_STATE;

@Entity
public class MealBookingEntity {
    @Id
    private String userId;
    private LocalDate date;
    private RESERVATION_STATE state;

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public RESERVATION_STATE getState() { return state; }
    public void setState(RESERVATION_STATE state) { this.state = state; }
}
