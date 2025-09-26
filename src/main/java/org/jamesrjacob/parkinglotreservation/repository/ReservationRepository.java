package org.jamesrjacob.parkinglotreservation.repository;

import org.jamesrjacob.parkinglotreservation.model.Reservation;
import org.jamesrjacob.parkinglotreservation.model.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Reservation r " +
            "WHERE r.slot = :slot AND r.startTime < :endTime AND r.endTime > :startTime")
    boolean existsBySlotAndTimeRange(@Param("slot") Slot slot,
                                     @Param("startTime") LocalDateTime startTime,
                                     @Param("endTime") LocalDateTime endTime);

    @Query("SELECT r FROM Reservation r WHERE r.slot.id = :slotId " +
            "AND r.startTime < :endTime AND r.endTime > :startTime")
    List<Reservation> findOverlappingReservations(@Param("slotId") Long slotId,
                                                  @Param("startTime") LocalDateTime startTime,
                                                  @Param("endTime") LocalDateTime endTime);
}
