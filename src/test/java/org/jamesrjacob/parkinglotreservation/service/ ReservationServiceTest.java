package org.jamesrjacob.parkinglotreservation.service;

import org.jamesrjacob.parkinglotreservation.TestDataFactory;
import org.jamesrjacob.parkinglotreservation.dto.ReservationRequestDTO;
import org.jamesrjacob.parkinglotreservation.dto.ReservationResponseDTO;
import org.jamesrjacob.parkinglotreservation.exception.InvalidReservationException;
import org.jamesrjacob.parkinglotreservation.exception.SlotAlreadyBookedException;
import org.jamesrjacob.parkinglotreservation.exception.SlotNotFoundException;
import org.jamesrjacob.parkinglotreservation.model.*;
import org.jamesrjacob.parkinglotreservation.repository.ReservationRepository;
import org.jamesrjacob.parkinglotreservation.repository.SlotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private SlotRepository slotRepository;

    @InjectMocks
    private ReservationService reservationService;

    private Floor floor;
    private Slot slot4Wheeler;
    private Slot slot2Wheeler;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @BeforeEach
    void setUp() {
        floor = TestDataFactory.createFloor(1L, "Ground Floor");
        slot4Wheeler = TestDataFactory.createSlot(1L, "G-01", VehicleType.FOUR_WHEELER, floor);
        slot2Wheeler = TestDataFactory.createSlot(2L, "G-02", VehicleType.TWO_WHEELER, floor);
        startTime = LocalDateTime.of(2024, 1, 15, 10, 0);
        endTime = LocalDateTime.of(2024, 1, 15, 12, 0);
    }

    @Test
    void reserveSlot_ValidRequest_ShouldCreateReservation() {
        // Arrange
        ReservationRequestDTO requestDTO = TestDataFactory.createReservationRequestDTO(
                "KA01AB1234", startTime, endTime, 1L);

        Reservation savedReservation = TestDataFactory.createReservation(
                1L, "KA01AB1234", startTime, endTime, slot4Wheeler, 60.0);

        when(slotRepository.findById(1L)).thenReturn(Optional.of(slot4Wheeler));
        when(reservationRepository.findOverlappingReservations(any(), any(), any()))
                .thenReturn(List.of());
        when(reservationRepository.save(any(Reservation.class))).thenReturn(savedReservation);

        // Act
        ReservationResponseDTO result = reservationService.reserveSlot(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals("KA01AB1234", result.getVehicleNumber());
        assertEquals(60.0, result.getCost()); // 2 hours * 30
        assertEquals(1L, result.getSlotId());
        assertEquals("G-01", result.getSlotNumber());

        verify(slotRepository).findById(1L);
        verify(reservationRepository).findOverlappingReservations(1L, startTime, endTime);
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    void reserveSlot_SlotNotFound_ShouldThrowException() {
        // Arrange
        ReservationRequestDTO requestDTO = TestDataFactory.createReservationRequestDTO(
                "KA01AB1234", startTime, endTime, 99L);

        when(slotRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(SlotNotFoundException.class, () -> reservationService.reserveSlot(requestDTO));
        verify(slotRepository).findById(99L);
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void reserveSlot_OverlappingReservation_ShouldThrowException() {
        // Arrange
        ReservationRequestDTO requestDTO = TestDataFactory.createReservationRequestDTO(
                "KA01AB1234", startTime, endTime, 1L);

        Reservation existingReservation = TestDataFactory.createReservation(
                1L, "MH02CD5678", startTime.minusHours(1), endTime.plusHours(1), slot4Wheeler, 90.0);

        when(slotRepository.findById(1L)).thenReturn(Optional.of(slot4Wheeler));
        when(reservationRepository.findOverlappingReservations(1L, startTime, endTime))
                .thenReturn(List.of(existingReservation));

        // Act & Assert
        assertThrows(SlotAlreadyBookedException.class, () -> reservationService.reserveSlot(requestDTO));
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void reserveSlot_InvalidVehicleNumber_ShouldThrowException() {
        // Arrange
        ReservationRequestDTO requestDTO = TestDataFactory.createReservationRequestDTO(
                "INVALID123", startTime, endTime, 1L);

        // Act & Assert
        assertThrows(InvalidReservationException.class, () -> reservationService.reserveSlot(requestDTO));
    }

    @Test
    void reserveSlot_StartTimeAfterEndTime_ShouldThrowException() {
        // Arrange
        ReservationRequestDTO requestDTO = TestDataFactory.createReservationRequestDTO(
                "KA01AB1234", endTime, startTime, 1L); // Reversed times

        // Act & Assert
        assertThrows(InvalidReservationException.class, () -> reservationService.reserveSlot(requestDTO));
    }

    @Test
    void reserveSlot_Exceeds24Hours_ShouldThrowException() {
        // Arrange
        ReservationRequestDTO requestDTO = TestDataFactory.createReservationRequestDTO(
                "KA01AB1234", startTime, startTime.plusHours(25), 1L);

        // Act & Assert
        assertThrows(InvalidReservationException.class, () -> reservationService.reserveSlot(requestDTO));
    }

    @Test
    void reserveSlot_PastStartTime_ShouldThrowException() {
        // Arrange
        LocalDateTime pastTime = LocalDateTime.of(2023, 1, 1, 10, 0);
        ReservationRequestDTO requestDTO = TestDataFactory.createReservationRequestDTO(
                "KA01AB1234", pastTime, pastTime.plusHours(2), 1L);

        // Act & Assert
        assertThrows(InvalidReservationException.class, () -> reservationService.reserveSlot(requestDTO));
    }

    @Test
    void reserveSlot_ConcurrentBooking_ShouldThrowException() {
        // Arrange
        ReservationRequestDTO requestDTO = TestDataFactory.createReservationRequestDTO(
                "KA01AB1234", startTime, endTime, 1L);

        when(slotRepository.findById(1L)).thenReturn(Optional.of(slot4Wheeler));
        when(reservationRepository.findOverlappingReservations(any(), any(), any()))
                .thenReturn(List.of());
        when(reservationRepository.save(any(Reservation.class)))
                .thenThrow(new ObjectOptimisticLockingFailureException(Reservation.class, 1L));

        // Act & Assert
        assertThrows(SlotAlreadyBookedException.class, () -> reservationService.reserveSlot(requestDTO));
    }

    @Test
    void calculateCost_4WheelerPartialHour_ShouldRoundUp() {
        // Arrange
        LocalDateTime start = LocalDateTime.of(2024, 1, 15, 10, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 15, 11, 15); // 1.25 hours

        // Act
        double cost = reservationService.calculateCost(VehicleType.FOUR_WHEELER, start, end);

        // Assert
        assertEquals(60.0, cost); // 2 hours * 30
    }

    @Test
    void calculateCost_2WheelerExactHours_ShouldCalculateCorrectly() {
        // Arrange
        LocalDateTime start = LocalDateTime.of(2024, 1, 15, 10, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 15, 13, 0); // 3 hours

        // Act
        double cost = reservationService.calculateCost(VehicleType.TWO_WHEELER, start, end);

        // Assert
        assertEquals(60.0, cost); // 3 hours * 20
    }

    @Test
    void getReservation_ExistingId_ShouldReturnReservation() {
        // Arrange
        Reservation reservation = TestDataFactory.createReservation(
                1L, "KA01AB1234", startTime, endTime, slot4Wheeler, 60.0);

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        // Act
        Optional<ReservationResponseDTO> result = reservationService.getReservation(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("KA01AB1234", result.get().getVehicleNumber());
        verify(reservationRepository).findById(1L);
    }

    @Test
    void getReservation_NonExistingId_ShouldReturnEmpty() {
        // Arrange
        when(reservationRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<ReservationResponseDTO> result = reservationService.getReservation(99L);

        // Assert
        assertFalse(result.isPresent());
        verify(reservationRepository).findById(99L);
    }

    @Test
    void cancelReservation_ExistingId_ShouldDelete() {
        // Arrange
        when(reservationRepository.existsById(1L)).thenReturn(true);

        // Act
        reservationService.cancelReservation(1L);

        // Assert
        verify(reservationRepository).deleteById(1L);
    }

    @Test
    void cancelReservation_NonExistingId_ShouldThrowException() {
        // Arrange
        when(reservationRepository.existsById(99L)).thenReturn(false);

        // Act & Assert
        assertThrows(InvalidReservationException.class, () -> reservationService.cancelReservation(99L));
        verify(reservationRepository, never()).deleteById(any());
    }

    @Test
    void getAllReservations_ShouldReturnPaginatedResults() {
        // Arrange
        Reservation reservation = TestDataFactory.createReservation(
                1L, "KA01AB1234", startTime, endTime, slot4Wheeler, 60.0);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Reservation> reservationPage = new PageImpl<>(List.of(reservation));

        when(reservationRepository.findAll(pageable)).thenReturn(reservationPage);

        // Act
        Page<ReservationResponseDTO> result = reservationService.getAllReservations(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(reservationRepository).findAll(pageable);
    }
}