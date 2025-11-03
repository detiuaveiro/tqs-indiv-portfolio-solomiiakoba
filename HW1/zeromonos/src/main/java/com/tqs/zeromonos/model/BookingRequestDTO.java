package com.tqs.zeromonos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
// extrair apenas o que é preciso
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestDTO {
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
}
