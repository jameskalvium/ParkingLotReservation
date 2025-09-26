package org.jamesrjacob.parkinglotreservation.service;

import org.jamesrjacob.parkinglotreservation.utils.TestDataFactory;
import org.jamesrjacob.parkinglotreservation.model.Floor;
import org.jamesrjacob.parkinglotreservation.model.Slot;
import org.jamesrjacob.parkinglotreservation.model.VehicleType;
import org.jamesrjacob.parkinglotreservation.repository.SlotRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SlotServiceTest {

    @Mock
    private SlotRepository slotRepository;

    @InjectMocks
    private SlotService slotService;

    @Test
    void createSlot_ValidSlot_ShouldSaveAndReturn() {
        // Arrange
        Floor floor = TestDataFactory.createFloor(1L, "Ground Floor");
        Slot slot = TestDataFactory.createSlot(null, "G-01", VehicleType.FOUR_WHEELER, floor);
        Slot savedSlot = TestDataFactory.createSlot(1L, "G-01", VehicleType.FOUR_WHEELER, floor);

        when(slotRepository.save(any(Slot.class))).thenReturn(savedSlot);

        // Act
        Slot result = slotService.createSlot(slot);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("G-01", result.getSlotNumber());
        verify(slotRepository).save(slot);
    }

    @Test
    void getAllSlots_ShouldReturnAllSlots() {
        // Arrange
        Floor floor = TestDataFactory.createFloor(1L, "Ground Floor");
        Slot slot1 = TestDataFactory.createSlot(1L, "G-01", VehicleType.FOUR_WHEELER, floor);
        Slot slot2 = TestDataFactory.createSlot(2L, "G-02", VehicleType.TWO_WHEELER, floor);

        when(slotRepository.findAll()).thenReturn(List.of(slot1, slot2));

        // Act
        List<Slot> result = slotService.getAllSlots();

        // Assert
        assertEquals(2, result.size());
        verify(slotRepository).findAll();
    }
}