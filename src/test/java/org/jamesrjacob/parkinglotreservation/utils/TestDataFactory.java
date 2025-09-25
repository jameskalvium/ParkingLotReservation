package org.jamesrjacob.parkinglotreservation.utils;

import org.jamesrjacob.parkinglotreservation.model.*;
import org.jamesrjacob.parkinglotreservation.dto.ReservationRequestDTO;

import java.time.LocalDateTime;

public class TestDataFactory {

    public static Floor createFloor(Long id, String name) {
        Floor floor = new Floor();
        floor.setId(id);
        floor.setName(name);
        return floor;
    }

    public static Slot createSlot(Long id, String slotNumber, VehicleType vehicleType, Floor floor) {
        Slot slot = new Slot();
        slot.setId(id);
        slot.setSlotNumber(slotNumber);
        slot.setVehicleType(vehicleType);
        slot.setFloor(floor);
        return slot;
    }

    public static Reservation createReservation(Long id, String vehicleNumber,
                                                LocalDateTime startTime, LocalDateTime endTime,
                                                Slot slot, double cost) {
        Reservation reservation = new Reservation();
        reservation.setId(id);
        reservation.setVehicleNumber(vehicleNumber);
        reservation.setStartTime(startTime);
        reservation.setEndTime(endTime);
        reservation.setSlot(slot);
        reservation.setCost(cost);
        reservation.setVersion(1L);
        return reservation;
    }

    public static ReservationRequestDTO createReservationRequestDTO(String vehicleNumber,
                                                                    LocalDateTime startTime,
                                                                    LocalDateTime endTime,
                                                                    Long slotId) {
        ReservationRequestDTO dto = new ReservationRequestDTO();
        dto.setVehicleNumber(vehicleNumber);
        dto.setStartTime(startTime);
        dto.setEndTime(endTime);
        dto.setSlotId(slotId);
        return dto;
    }

    // Helper method to get fixed test time
    public static LocalDateTime fixedDateTime() {
        return LocalDateTime.of(2024, 1, 15, 10, 0);
    }
}