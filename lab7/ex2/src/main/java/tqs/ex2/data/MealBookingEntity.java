package tqs.ex2.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDate;
import tqs.ex2.model.RESERVATION_STATE;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class MealBookingEntity {
    @Id
    private String userId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;
    private RESERVATION_STATE state;
    public MealBookingEntity(String userId, LocalDate date, RESERVATION_STATE state) {
        this.userId = userId;
        this.date = date;
        this.state = state;
    }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public RESERVATION_STATE getState() { return state; }
    public void setState(RESERVATION_STATE state) { this.state = state; }
}
