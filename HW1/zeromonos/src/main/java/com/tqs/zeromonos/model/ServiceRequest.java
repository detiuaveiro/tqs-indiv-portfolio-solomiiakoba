package com.tqs.zeromonos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.*;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="service_requests")
public class ServiceRequest {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String token;

    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters")
    @Column(nullable = false,  length = 500)
    private String description;

    @NotBlank(message = "Municipality is required")
    @Column(nullable = false)
    private String municipality;

    @NotNull(message = "Collection date is required")
    private LocalDate collectionDate;

    @NotNull(message = "Time slot is required")
    @Enumerated(EnumType.STRING)
    private TimeSlot timeSlot;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status currentState = Status.RECEIVED;

    @OneToMany(mappedBy = "serviceRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StatusHistory> statusHistory = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.token == null) {
            this.token = UUID.randomUUID().toString();  // generate token
        }
        addStatusHistory(this.currentState);
    }
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    public void updateStatus(Status newStatus) {
        if (this.currentState != newStatus) {
            this.currentState = newStatus;
            addStatusHistory(newStatus);
        }
    }
    private void addStatusHistory(Status newStatus) {
        StatusHistory statusHistory = new StatusHistory();
        statusHistory.setServiceRequest(this);
        statusHistory.setStatus(newStatus);
        statusHistory.setTimestamp(LocalDateTime.now());
        this.statusHistory.add(statusHistory);
    }
    public boolean canBeCancelled() {
        return this.currentState == Status.RECEIVED || this.currentState == Status.ASSIGNED;
    }
}
