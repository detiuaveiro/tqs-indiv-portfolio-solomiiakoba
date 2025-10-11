package tqs.lab3meals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import tqs.lab3meals.data.MealBookingEntity;
import tqs.lab3meals.data.MealBookingRepository;

import org.mockito.*;
import tqs.lab3meals.model.RESERVATION_STATE;
import tqs.lab3meals.service.MealsBookingService;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MealBookingServiceTest {
    // inject a test-friendly Entity Manager
    @InjectMocks
    private MealsBookingService bookingService;

    @Mock
    private MealBookingRepository repository;

    private MealBookingEntity mealBooking;

    @BeforeEach
    void setUp() {
        mealBooking = new MealBookingEntity();
        mealBooking.setUserId("user1");
        mealBooking.setDate(LocalDate.now());
        mealBooking.setState(RESERVATION_STATE.RESERVADO);
    }

    @Test
    void testBookMeal_Success() {
        when(repository.existsById("user1")).thenReturn(false);
        String res = bookingService.bookMeal(mealBooking);
        assertEquals("user1", res);
        assertEquals(RESERVATION_STATE.RESERVADO, mealBooking.getState());
        verify(repository, times(1)).save(mealBooking);
    }

    @Test
    void testBookMeal_Failure() {
        when(repository.existsById("user1")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> bookingService.bookMeal(mealBooking));
        verify(repository, never()).save(any());
    }

    @Test
    void testFindRes_Found() {
        when(repository.findById("user1")).thenReturn(Optional.of(mealBooking));
        Optional<MealBookingEntity> found = bookingService.findReservation("user1");
        assertTrue(found.isPresent());
        assertEquals(mealBooking, found.get());

    }

    @Test
    void testFindRes_NotFound() {
        when(repository.findById("user1")).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> bookingService.cancelReservation("user1"));
    }

    @Test
    void testCancelReservation_Success() {
        when(repository.findById("user1")).thenReturn(Optional.of(mealBooking));
        bookingService.cancelReservation("user1");
        assertEquals(RESERVATION_STATE.CANCELADO, mealBooking.getState());
        verify(repository, times(1)).save(mealBooking);
    }

    @Test
    void testCancelReservation_Failure() {
        when(repository.findById("user1")).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> bookingService.cancelReservation("user1"));
    }

    @Test
    void testCheckIns_Success() {
        when(repository.findById("user1")).thenReturn(Optional.of(mealBooking));
        bookingService.checkIn("user1");
        assertEquals(RESERVATION_STATE.USADO, mealBooking.getState());
        verify(repository).save(mealBooking);
    }

    @Test
    void testCheckIns_Failure() {
        mealBooking.setState(RESERVATION_STATE.CANCELADO);
        when(repository.findById("user1")).thenReturn(Optional.of(mealBooking));
        assertThrows(IllegalArgumentException.class, () -> bookingService.checkIn("user1"));
        verify(repository, never()).save(any());
    }

    @Test
    void testGetAllBookings() {
        when(repository.findAll()).thenReturn(List.of(mealBooking));
        List<MealBookingEntity> res = bookingService.getAllReservations();
        assertEquals(1, res.size());
        assertEquals("user1", res.get(0).getUserId());
        verify(repository, times(1)).findAll();
    }

}
