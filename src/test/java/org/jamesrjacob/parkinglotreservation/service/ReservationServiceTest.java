package org.jamesrjacob.parkinglotreservation.service;

import org.jamesrjacob.parkinglotreservation.dto.ReservationRequestDTO;
import org.jamesrjacob.parkinglotreservation.model.Reservation;
import org.jamesrjacob.parkinglotreservation.model.Slot;
import org.jamesrjacob.parkinglotreservation.model.VehicleType;
import org.jamesrjacob.parkinglotreservation.repository.ReservationRepository;
import org.jamesrjacob.parkinglotreservation.repository.SlotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

    private ReservationRepository reservationRepository;
    private SlotRepository slotRepository;
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        reservationRepository = mock(ReservationRepository.class);
        slotRepository = mock(SlotRepository.class);
        reservationService = new ReservationService(reservationRepository, slotRepository);
    }

    @Test
    void reserveSlot_success() {
        Slot slot = new Slot();
        slot.setId(1L);
        slot.setSlotNumber("A1");

        when(slotRepository.findById(1L)).thenReturn(Optional.of(slot));
        when(reservationRepository.existsBySlotAndTimeRange(any(), any(), any())).thenReturn(false);

        ReservationRequestDTO request = new ReservationRequestDTO();
        request.setSlotId(1L);
        request.setVehicleType(VehicleType.FOUR_WHEELER);
        request.setVehicleNumber("KA-01-1234");
        request.setStartTime(LocalDateTime.now().plusHours(1));
        request.setEndTime(LocalDateTime.now().plusHours(2));

        var response = reservationService.reserveSlot(request);
        assertNotNull(response);
        assertEquals("A1", response.getSlotNumber());
    }

    @Test
    void reserveSlot_slotNotFound_shouldThrow() {
        when(slotRepository.findById(1L)).thenReturn(Optional.empty());

        ReservationRequestDTO request = new ReservationRequestDTO();
        request.setSlotId(1L);
        request.setVehicleType(VehicleType.FOUR_WHEELER);
        request.setVehicleNumber("KA-01-1234");
        request.setStartTime(LocalDateTime.now().plusHours(1));
        request.setEndTime(LocalDateTime.now().plusHours(2));

        assertThrows(IllegalArgumentException.class, () -> reservationService.reserveSlot(request));
    }
}
