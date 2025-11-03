package com.tqs.zeromonos.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.*;
import java.util.*;

@Repository
public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Long> {
    // queries automaticas
    Optional<ServiceRequest> findByToken(String token);
    List<ServiceRequest> findByMunicipality(String municipality);
    List<ServiceRequest> findByMunicipalityAndCurrentState(String municipality, Status status);
    List<ServiceRequest> findByCurrentState(Status currentState);

    // queries especificas
    @Query("SELECT COUNT(sr) FROM ServiceRequest sr WHERE sr.collectionDate = :date AND sr.municipality = :municipality AND sr.currentState NOT IN ('CANCELED', 'COMPLETED')")
    long countActiveRequestsByDateAndMunicipality(LocalDate date, String municipality);

    @Query("SELECT COUNT(sr) FROM ServiceRequest sr WHERE sr.collectionDate = :date AND sr.municipality = :municipality AND sr.currentState NOT IN ('CANCELED', 'COMPLETED') AND sr.timeSlot = :timeSlot")
    long countActiveRequestsByDateAndMunicipalityAndTimeSlot(LocalDate date, String municipality, TimeSlot timeSlot);

    List<ServiceRequest> findByCollectionDateBetween(LocalDateTime start, LocalDateTime end);

    // todos os pedidos de um status
    @Query("SELECT sr FROM ServiceRequest sr WHERE sr.currentState IN :statuses ORDER BY sr.createdAt DESC")
    long findByCurrentStateIn(@Param("statuses") List<Status> statuses);
}
