package org.jamesrjacob.parkinglotreservation.repository;

import org.jamesrjacob.parkinglotreservation.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r WHERE r.slot.id = :slotId AND " +
            "((r.startTime <= :endTime AND r.endTime >= :startTime))")
    List<Reservation> findOverlappingReservations(@Param("slotId") Long slotId,
                                                  @Param("startTime") LocalDateTime startTime,
                                                  @Param("endTime") LocalDateTime endTime);

    @Query("SELECT r FROM Reservation r WHERE r.slot.id = :slotId AND r.id != :reservationId AND " +
            "((r.startTime <= :endTime AND r.endTime >= :startTime))")
    List<Reservation> findOverlappingReservationsExcludingCurrent(@Param("slotId") Long slotId,
                                                                  @Param("startTime") LocalDateTime startTime,
                                                                  @Param("endTime") LocalDateTime endTime,
                                                                  @Param("reservationId") Long reservationId);

    List<Reservation> findBySlotId(Long slotId);
}