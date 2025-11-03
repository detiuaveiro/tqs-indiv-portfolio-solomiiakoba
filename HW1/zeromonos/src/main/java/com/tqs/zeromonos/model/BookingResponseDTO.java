package com.tqs.zeromonos.model;
import lombok.*;
import java.time.*;
import java.util.*;

// dto para retornar info aos clientes detalhes de booking + status history
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDTO {

    // Basic booking information
    private Long id;
    private String token;
    private String description;
    private String municipality;
    private LocalDate collectionDate;
    private TimeSlot timeSlot;
    private Status currentState;

    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Status history with all transitions
    private List<StatusHistoryDTO> statusHistory = new ArrayList<>();

    // dto interna para historico
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatusHistoryDTO {
        private Status status;
        private LocalDateTime timestamp;
        private String statusDescription;

        // Constructor for easy mapping
        public StatusHistoryDTO(Status status, LocalDateTime timestamp) {
            this.status = status;
            this.timestamp = timestamp;
            this.statusDescription = status.getDescription();
        }
    }
}

