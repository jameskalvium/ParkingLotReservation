package org.jamesrjacob.parkinglotreservation.repository;

import org.jamesrjacob.parkinglotreservation.model.Slot;
import org.jamesrjacob.parkinglotreservation.model.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface SlotRepository extends JpaRepository<Slot, Long> {

    List<Slot> findByFloorId(Long floorId);
    List<Slot> findByVehicleType(VehicleType vehicleType);

    @Query("SELECT s FROM Slot s WHERE s.vehicleType = :vehicleType AND s.id NOT IN " +
            "(SELECT r.slot.id FROM Reservation r WHERE " +
            "(r.startTime <= :endTime AND r.endTime >= :startTime))")
    List<Slot> findAvailableSlotsByVehicleTypeAndTimeRange(@Param("vehicleType") VehicleType vehicleType,
                                                           @Param("startTime") LocalDateTime startTime,
                                                           @Param("endTime") LocalDateTime endTime);

    @Query("SELECT s FROM Slot s WHERE s.id NOT IN " +
            "(SELECT r.slot.id FROM Reservation r WHERE " +
            "(r.startTime <= :endTime AND r.endTime >= :startTime))")
    List<Slot> findAllAvailableSlotsByTimeRange(@Param("startTime") LocalDateTime startTime,
                                                @Param("endTime") LocalDateTime endTime);
}