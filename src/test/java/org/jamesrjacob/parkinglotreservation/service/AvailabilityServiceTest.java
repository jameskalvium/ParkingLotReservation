package org.jamesrjacob.parkinglotreservation.service;

import org.jamesrjacob.parkinglotreservation.utils.TestDataFactory;
import org.jamesrjacob.parkinglotreservation.dto.SlotAvailabilityResponseDTO;
import org.jamesrjacob.parkinglotreservation.model.Floor;
import org.jamesrjacob.parkinglotreservation.model.Slot;
import org.jamesrjacob.parkinglotreservation.model.VehicleType;
import org.jamesrjacob.parkinglotreservation.repository.SlotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AvailabilityServiceTest {

    @Mock
    private SlotRepository slotRepository;

    @InjectMocks
    private AvailabilityService availabilityService;

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
        startTime = TestDataFactory.futureStartTime();
        endTime = TestDataFactory.futureEndTime();
    }

    @Test
    void getAvailableSlots_NoVehicleTypeFilter_ShouldReturnAllAvailableSlots() {
        // Arrange
        when(slotRepository.findAllAvailableSlotsByTimeRange(startTime, endTime))
                .thenReturn(List.of(slot4Wheeler, slot2Wheeler));

        // Act
        List<SlotAvailabilityResponseDTO> result =
                availabilityService.getAvailableSlots(startTime, endTime, null);

        // Assert
        assertEquals(2, result.size());
        verify(slotRepository).findAllAvailableSlotsByTimeRange(startTime, endTime);
    }

    @Test
    void getAvailableSlots_WithVehicleTypeFilter_ShouldReturnFilteredSlots() {
        // Arrange
        when(slotRepository.findAvailableSlotsByVehicleTypeAndTimeRange(VehicleType.FOUR_WHEELER, startTime, endTime))
                .thenReturn(List.of(slot4Wheeler));

        // Act
        List<SlotAvailabilityResponseDTO> result =
                availabilityService.getAvailableSlots(startTime, endTime, VehicleType.FOUR_WHEELER);

        // Assert
        assertEquals(1, result.size());
        assertEquals(VehicleType.FOUR_WHEELER, result.get(0).getVehicleType());
    }

    @Test
    void getAvailableSlots_InvalidTimeRange_ShouldThrowException() {
        // Arrange
        LocalDateTime invalidStartTime = endTime;
        LocalDateTime invalidEndTime = startTime;

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> availabilityService.getAvailableSlots(invalidStartTime, invalidEndTime, null));
    }

    @Test
    void getAvailableSlots_PastStartTime_ShouldThrowException() {
        // Arrange
        LocalDateTime pastTime = LocalDateTime.now().minusHours(1);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> availabilityService.getAvailableSlots(pastTime, endTime, null));
    }
}