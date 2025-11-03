package com.tqs.zeromonos.service;

import com.tqs.zeromonos.exception.BookingNotFoundException;
import com.tqs.zeromonos.exception.InvalidBookingException;
import com.tqs.zeromonos.exception.InvalidStatusTransitionException;
import com.tqs.zeromonos.exception.ServiceLimitExceededException;
import com.tqs.zeromonos.model.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BookingService {
    private final ServiceRequestRepository repository;
    private final MunicipalityService municipalityService;

    @Value("${booking.max-request-per-day:63}")
    private int maxRequestsPerDay;

    @Value("${booking.max-request-per-timeslot:21}")
    private int maxRequestsPerTimeSlot;

    public BookingService(ServiceRequestRepository repository, MunicipalityService municipalityService) {
        this.repository = repository;
        this.municipalityService = municipalityService;
    }

    @Transactional
    public BookingResponseDTO createBooking(BookingRequestDTO request) {
        log.info("Booking created for {}", request.getMunicipality());
        if(!municipalityService.isValidMunicipality((request.getMunicipality()))) {
            throw new InvalidBookingException("Invalid municipality");
        }
        validateRequestDate(request.getCollectionDate());
        validateServiceLimits(request);

        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setDescription(request.getDescription());
        serviceRequest.setMunicipality(request.getMunicipality());
        serviceRequest.setCollectionDate(request.getCollectionDate());
        serviceRequest.setTimeSlot(request.getTimeSlot());
        ServiceRequest savedRequest = repository.save(serviceRequest);
        log.info("Saved service request with token: {}", savedRequest.getToken());
        return mapToDTO(savedRequest);
    }

    @Transactional
    public BookingResponseDTO getBookingByToken(String token) {
        log.info("Getting booking by token {}", token);
        ServiceRequest request = repository.findByToken(token).orElseThrow(() -> new BookingNotFoundException("Booking not found with token: " + token));
        return mapToDTO(request);
    }
    @Transactional
    public List<BookingResponseDTO> getAllBookings() {
        log.info("Getting all bookings");
        return repository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    @Transactional
    public List<BookingResponseDTO> getBookingsByMunicipality(String municipality) {
        log.info("Getting booking by municipality {}", municipality);
        return repository.findByMunicipality(municipality).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    @Transactional
    public BookingResponseDTO updateBookingStatus(String token, StatusUpdateDTO statusUpdate) {
        log.info("Updating booking {} to status {}", token, statusUpdate.getStatus());
        ServiceRequest request = repository.findByToken(token).
                orElseThrow(() -> new BookingNotFoundException("Booking not found with token: " + token));
        if (!request.getCurrentState().canTransitionTo(statusUpdate.getStatus())) {
            throw new InvalidStatusTransitionException("Cannot do transition to " + statusUpdate.getStatus());
        }
        request.updateStatus(statusUpdate.getStatus());
        ServiceRequest updated = repository.save(request);
        log.info("Booking {} status updated to {}", token, statusUpdate.getStatus());
        return mapToDTO(updated);
    }
    @Transactional
    public void cancelBooking(String token) {
        log.info("Cancelling booking {}", token);
        ServiceRequest request = repository.findByToken(token).orElseThrow(() -> new BookingNotFoundException("Booking not found with token: " + token));
        if (!request.canBeCancelled()) {
            throw new InvalidBookingException("Cannot cancel booking");
        }
        request.updateStatus(Status.CANCELED);
        repository.save(request);
        log.info("Booking {} cancelled with status {}", token, request.getCurrentState());
    }

    private void validateRequestDate(LocalDate date) {
        if (date.isBefore(LocalDate.now().plusDays(1))) {
            throw new InvalidBookingException("Invalid date range. Booking must be at least 1 day in advance.");
        }
        if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new InvalidBookingException("Invalid date range.");
        }
    }
    private void validateServiceLimits(BookingRequestDTO request) {
        long dailyCount = repository.countActiveRequestsByDateAndMunicipality(request.getCollectionDate(), request.getMunicipality());
        if (dailyCount >= maxRequestsPerDay) {
            throw new ServiceLimitExceededException(
                    String.format("Daily limit reached for %s on %s", request.getMunicipality(), request.getCollectionDate()));
        }
        //
        long timeSlotCount = repository.countActiveRequestsByDateAndMunicipalityAndTimeSlot(request.getCollectionDate(), request.getMunicipality(), request.getTimeSlot());
        if (timeSlotCount >= maxRequestsPerTimeSlot) {
            throw new ServiceLimitExceededException(
                    String.format("Time slot %s is fully booked for %s on %s", request.getTimeSlot(), request.getMunicipality(), request.getCollectionDate())

            );

        }
    }
    private BookingResponseDTO mapToDTO(ServiceRequest request) {
        BookingResponseDTO dto = new BookingResponseDTO();
        dto.setId(request.getId());
        dto.setToken(request.getToken());
        dto.setDescription(request.getDescription());
        dto.setMunicipality(request.getMunicipality());
        dto.setCollectionDate(request.getCollectionDate());
        dto.setTimeSlot(request.getTimeSlot());
        dto.setCurrentState(request.getCurrentState());
        dto.setCreatedAt(request.getCreatedAt());
        dto.setUpdatedAt(request.getUpdatedAt());

        // Map status history
        List<BookingResponseDTO.StatusHistoryDTO> history =
                request.getStatusHistory().stream()
                        .map(h -> new BookingResponseDTO.StatusHistoryDTO(
                                h.getStatus(),
                                h.getTimestamp(),
                                h.getStatus().getDescription()))
                        .collect(Collectors.toList());
        dto.setStatusHistory(history);

        return dto;
    }
}
