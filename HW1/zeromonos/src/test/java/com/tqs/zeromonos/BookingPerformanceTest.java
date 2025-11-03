package com.tqs.zeromonos;

import com.tqs.zeromonos.model.BookingRequestDTO;
import com.tqs.zeromonos.model.TimeSlot;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class BookingPerformanceTest {

    private static final String BASE_URL = "http://localhost:8080/api";
    private static final int CONCURRENT_USERS = 10;

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    void testConcurrentBookingCreation() throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(CONCURRENT_USERS);
        List<Future<Long>> futures = new ArrayList<>();

        long startTime = System.currentTimeMillis();

        // Submit concurrent booking requests
        for (int i = 0; i < CONCURRENT_USERS; i++) {
            final int userId = i;
            Future<Long> future = executor.submit(() -> {
                return createBookingAndMeasure(userId);
            });
            futures.add(future);
        }

        // Collect results
        List<Long> responseTimes = new ArrayList<>();
        for (Future<Long> future : futures) {
            responseTimes.add(future.get());
        }

        long totalTime = System.currentTimeMillis() - startTime;
        executor.shutdown();

        // Calculate statistics
        double avgResponseTime = responseTimes.stream()
                .mapToLong(Long::longValue)
                .average()
                .orElse(0.0);

        long maxResponseTime = responseTimes.stream()
                .mapToLong(Long::longValue)
                .max()
                .orElse(0L);

        long minResponseTime = responseTimes.stream()
                .mapToLong(Long::longValue)
                .min()
                .orElse(0L);

        // Print results
        System.out.println("\n========== PERFORMANCE TEST RESULTS ==========");
        System.out.println("Total concurrent users: " + CONCURRENT_USERS);
        System.out.println("Total execution time: " + totalTime + " ms");
        System.out.println("Average response time: " + avgResponseTime + " ms");
        System.out.println("Min response time: " + minResponseTime + " ms");
        System.out.println("Max response time: " + maxResponseTime + " ms");
        System.out.println("Throughput: " + (CONCURRENT_USERS * 1000.0 / totalTime) + " requests/second");
        System.out.println("==============================================\n");

        assertThat(avgResponseTime).isLessThan(2000); // Average should be under 2 seconds
        assertThat(maxResponseTime).isLessThan(5000); // Max should be under 5 seconds
    }

    @Test
    void testApiResponseTimeUnderLoad() {
        int numberOfRequests = 50;
        List<Long> responseTimes = new ArrayList<>();

        for (int i = 0; i < numberOfRequests; i++) {
            long startTime = System.currentTimeMillis();

            given()
                    .contentType(ContentType.JSON)
                    .when()
                    .get("/municipalities")
                    .then()
                    .statusCode(200);

            long responseTime = System.currentTimeMillis() - startTime;
            responseTimes.add(responseTime);
        }

        double avgResponseTime = responseTimes.stream()
                .mapToLong(Long::longValue)
                .average()
                .orElse(0.0);

        System.out.println("\n========== GET MUNICIPALITIES PERFORMANCE ==========");
        System.out.println("Number of requests: " + numberOfRequests);
        System.out.println("Average response time: " + avgResponseTime + " ms");
        System.out.println("===================================================\n");

        assertThat(avgResponseTime).isLessThan(500); // Should respond quickly
    }

    @Test
    void testDatabaseQueryPerformance() {
        // Create some test data first
        for (int i = 0; i < 9; i++) {
            createTestBooking(i);
        }

        // Measure query performance
        long startTime = System.currentTimeMillis();

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/bookings")
                .then()
                .statusCode(200);

        long queryTime = System.currentTimeMillis() - startTime;

        System.out.println("\n========== DATABASE QUERY PERFORMANCE ==========");
        System.out.println("Query time for all bookings: " + queryTime + " ms");
        System.out.println("===============================================\n");

        assertThat(queryTime).isLessThan(1000); // Query should be fast
    }

    // Helper methods
    private long createBookingAndMeasure(int userId) {
        BookingRequestDTO request = createBookingRequest(userId);

        long startTime = System.currentTimeMillis();

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/bookings")
                .then()
                .statusCode(201);

        return System.currentTimeMillis() - startTime;
    }

    private void createTestBooking(int index) {
        BookingRequestDTO request = createBookingRequest(index);

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/bookings")
                .then()
                .statusCode(201);
    }

    private BookingRequestDTO createBookingRequest(int index) {
        BookingRequestDTO request = new BookingRequestDTO();
        request.setMunicipality("Aveiro");
        request.setDescription("Performance test booking number " + index + " with sufficient description");
        request.setCollectionDate(getValidDate(index));
        request.setTimeSlot(index % 2 == 0 ? TimeSlot.MORNING : TimeSlot.AFTERNOON);
        return request;
    }

    private LocalDate getValidDate(int offsetDays) {
        LocalDate date = LocalDate.now().plusDays(2 + offsetDays);
        while (date.getDayOfWeek().getValue() >= 6) {
            date = date.plusDays(1);
        }
        return date;
    }
}
