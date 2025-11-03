package com.tqs.zeromonos;
import com.tqs.zeromonos.exception.BookingNotFoundException;
import com.tqs.zeromonos.exception.InvalidBookingException;
import com.tqs.zeromonos.exception.ServiceLimitExceededException;
import com.tqs.zeromonos.model.*;
import com.tqs.zeromonos.service.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.*;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)// For integration of Mockito framework
@MockitoSettings(strictness = Strictness.LENIENT)
class BookingServiceTest {
    @Mock
    private ServiceRequestRepository repository; // Mock do serviço externo
    @Mock
    private MunicipalityService municipalityService;
    @InjectMocks
    private BookingService bookingService;
    private BookingRequestDTO validRequest;
    private ServiceRequest serviceRequest;

    @BeforeEach
    void setUp() {
        bookingService = new BookingService(repository, municipalityService);
        ReflectionTestUtils.setField(bookingService, "maxRequestsPerDay", 9);
        ReflectionTestUtils.setField(bookingService, "maxRequestsPerTimeSlot", 3);
        validRequest = new BookingRequestDTO();
        validRequest.setDescription("Old sofa and mattress");
        validRequest.setMunicipality("Aveiro");
        validRequest.setCollectionDate(getNextWeekday());
        validRequest.setTimeSlot(TimeSlot.MORNING);

        serviceRequest = new ServiceRequest();
        serviceRequest.setId(1L);
        serviceRequest.setToken("token");
        serviceRequest.setDescription(validRequest.getDescription());
        serviceRequest.setMunicipality(validRequest.getMunicipality());
        serviceRequest.setCollectionDate(validRequest.getCollectionDate());
        serviceRequest.setTimeSlot(validRequest.getTimeSlot());
    }
    private LocalDate getNextWeekday() {
        LocalDate date = LocalDate.now().plusDays(2);
        while (date.getDayOfWeek() == DayOfWeek.SATURDAY ||
                date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            date = date.plusDays(1);
        }
        return date;
    }
    @Test
    void createValidBooking() {
        when(municipalityService.isValidMunicipality("Aveiro")).thenReturn(true);
        when(repository.countActiveRequestsByDateAndMunicipality(any(), any())).thenReturn(5L);
        when(repository.countActiveRequestsByDateAndMunicipalityAndTimeSlot(any(), any(), any())).thenReturn(2L);
        when(repository.save(any(ServiceRequest.class))).thenReturn(serviceRequest);

        BookingResponseDTO response = bookingService.createBooking(validRequest);
        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("token");
        assertThat(response.getMunicipality()).isEqualTo("Aveiro");
        verify(repository, times(1)).save(any(ServiceRequest.class));
    }
    @Test
    void createInvalidBooking() {
        when(municipalityService.isValidMunicipality("Wrong city")).thenReturn(false);
        validRequest.setMunicipality("Wrong city");
        assertThatThrownBy(() -> bookingService.createBooking(validRequest))
                .isInstanceOf(InvalidBookingException.class)
                .hasMessageContaining("Invalid municipality");

        verify(repository, never()).save(any());
    }
    @Test
    void dailyLimitExceeded_ThrowException() {
        when(municipalityService.isValidMunicipality(any())).thenReturn(true);
        when(repository.countActiveRequestsByDateAndMunicipality(any(), any())).thenReturn(10L);
        assertThatThrownBy(() -> bookingService.createBooking(validRequest))
                .isInstanceOf(ServiceLimitExceededException.class)
                .hasMessageContaining("Daily limit reached");

        verify(repository, never()).save(any());
    }

    @Test
    void bookingOnWeekend_ThrowException() {
        LocalDate date = LocalDate.now().plusDays(1);
        while (date.getDayOfWeek() != DayOfWeek.SATURDAY) {
            date = date.plusDays(1);
        }
        LocalDate saturday = date;
        validRequest.setCollectionDate(saturday);
        when(municipalityService.isValidMunicipality(any())).thenReturn(true);
        assertThatThrownBy(() -> bookingService.createBooking(validRequest))
                .isInstanceOf(InvalidBookingException.class)
                .hasMessageContaining("Invalid date range");

        verify(repository, never()).save(any());
    }
    @Test
    void timeSlotLimitExceeded_ThrowException() {
        when(municipalityService.isValidMunicipality(any())).thenReturn(true);
        when(repository.countActiveRequestsByDateAndMunicipality(any(), any())).thenReturn(5L);
        when(repository.countActiveRequestsByDateAndMunicipalityAndTimeSlot(any(), any(), any())).thenReturn(3L);

        assertThatThrownBy(() -> bookingService.createBooking(validRequest))
                .isInstanceOf(ServiceLimitExceededException.class)
                .hasMessageContaining("fully booked");

        verify(repository, never()).save(any());
    }
    @Test
    void bookingByValidToke() {
        when(repository.findByToken("token")).thenReturn(Optional.of(serviceRequest));
        BookingResponseDTO response = bookingService.getBookingByToken("token");
        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("token");
    }
    @Test
    void bookingByInvalidToken() {
        when(repository.findByToken("invalid-token-token")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> bookingService.getBookingByToken("invalid-token-token"))
                .isInstanceOf(BookingNotFoundException.class);
    }
    @Test
    void cancelBookingByValidToken() {
        serviceRequest.setCurrentState(Status.RECEIVED);
        when(repository.findByToken("token")).thenReturn(Optional.of(serviceRequest));
        when(repository.save(any())).thenReturn(serviceRequest);
        bookingService.cancelBooking("token");
        verify(repository, times(1)).save(argThat(sr ->
                sr.getCurrentState() == Status.CANCELED));
    }
    @Test
    void cancelBookingByInvalidToken() {
        serviceRequest.setCurrentState(Status.COMPLETED);
        when(repository.findByToken("invalid-token-token")).thenReturn(Optional.of(serviceRequest));
        when(repository.save(any())).thenReturn(serviceRequest);
        assertThatThrownBy(() -> bookingService.cancelBooking("invalid-token-token")).isInstanceOf(InvalidBookingException.class).hasMessageContaining("Cannot cancel booking");
    }
}
