package tqs.lab3meals;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import tqs.lab3meals.data.MealBookingEntity;
import tqs.lab3meals.data.MealBookingRepository;
import tqs.lab3meals.model.RESERVATION_STATE;

import java.time.LocalDate;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MealBookingRestAssuredIT {

    @LocalServerPort
    private int port;

    @Autowired
    private MealBookingRepository repository;

    @BeforeEach
    void setup() { RestAssured.port = port;}
    @AfterEach
    void resetDb() {
        repository.deleteAll();
    }

    @Test
    void testGetBooking() {
        MealBookingEntity booking = new MealBookingEntity();
        booking.setUserId("user2");
        booking.setDate(LocalDate.now());
        booking.setState(RESERVATION_STATE.RESERVADO);

        // cria a reserva primeiro
        given()
                .contentType(ContentType.JSON)
                .body(booking)
                .when()
                .post("/api/bookings")
                .then()
                .statusCode(201);

        // verifica se foi criada com sucesso
        when()
                .get("/api/bookings/user2")
                .then()
                .statusCode(200)
                .body("userId", equalTo("user2"))
                .body("state", equalTo("RESERVADO"));
    }

    @Test
    void testCreateBooking_Success() {
        MealBookingEntity booking = new MealBookingEntity();
        booking.setUserId("user10");
        booking.setDate(LocalDate.now());
        booking.setState(RESERVATION_STATE.RESERVADO);

        given()
                .contentType(ContentType.JSON)
                .body(booking)
                .when()
                .post("/api/bookings")
                .then()
                .statusCode(201)
                .contentType("text/plain")
                .body(equalTo("user10"));
    }

    @Test
    void testGetAllBookings() {
        // cria uma reserva antes
        MealBookingEntity booking = new MealBookingEntity();
        booking.setUserId("user11");
        booking.setDate(LocalDate.now());
        booking.setState(RESERVATION_STATE.RESERVADO);

        given()
                .contentType(ContentType.JSON)
                .body(booking)
                .when()
                .post("/api/bookings")
                .then()
                .statusCode(201);

        when()
                .get("/api/bookings")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("$", not(empty())); // verifica que há pelo menos 1
    }

    @Test
    void testGetBookingById_NotFound() {
        when()
                .get("/api/bookings/naoexiste")
                .then()
                .statusCode(404)
                .body(containsString("Booking not found"));
    }

    @Test
    void testCheckIn_Success() {
        MealBookingEntity booking = new MealBookingEntity();
        booking.setUserId("user12");
        booking.setDate(LocalDate.now());
        booking.setState(RESERVATION_STATE.RESERVADO);

        // cria
        given()
                .contentType(ContentType.JSON)
                .body(booking)
                .when()
                .post("/api/bookings")
                .then()
                .statusCode(201);

        // faz check-in
        when()
                .put("/api/bookings/user12/checkin")
                .then()
                .statusCode(200)
                .body(equalTo("Booking checked in successfully"));
    }
}

