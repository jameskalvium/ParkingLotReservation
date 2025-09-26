package org.jamesrjacob.parkinglotreservation.service;

import org.jamesrjacob.parkinglotreservation.dto.SlotRequestDTO;
import org.jamesrjacob.parkinglotreservation.dto.SlotResponseDTO;
import org.jamesrjacob.parkinglotreservation.model.Floor;
import org.jamesrjacob.parkinglotreservation.model.Slot;
import org.jamesrjacob.parkinglotreservation.model.VehicleType;
import org.jamesrjacob.parkinglotreservation.repository.FloorRepository;
import org.jamesrjacob.parkinglotreservation.repository.SlotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SlotServiceTest {

    private SlotRepository slotRepository;
    private FloorRepository floorRepository;
    private SlotService slotService;

    @BeforeEach
    void setUp() {
        slotRepository = mock(SlotRepository.class);
        floorRepository = mock(FloorRepository.class);
        slotService = new SlotService(slotRepository, floorRepository);
    }

    @Test
    void createSlot_success() {
        Floor floor = new Floor();
        floor.setId(1L);
        floor.setName("Ground Floor");

        SlotRequestDTO request = new SlotRequestDTO();
        request.setSlotNumber("A1");
        request.setVehicleType(VehicleType.FOUR_WHEELER);
        request.setFloorId(1L);

        when(floorRepository.findById(1L)).thenReturn(Optional.of(floor));

        Slot savedSlot = new Slot();
        savedSlot.setId(1L);
        savedSlot.setSlotNumber("A1");
        savedSlot.setVehicleType(VehicleType.FOUR_WHEELER);
        savedSlot.setFloor(floor);

        when(slotRepository.save(any(Slot.class))).thenReturn(savedSlot);

        SlotResponseDTO response = slotService.createSlot(request);

        assertEquals("A1", response.getSlotNumber());
        assertEquals("Ground Floor", response.getFloorName());
        assertEquals(VehicleType.FOUR_WHEELER, response.getVehicleType());
    }

    @Test
    void getAllSlots_returnsList() {
        Floor floor = new Floor();
        floor.setId(1L);
        floor.setName("Ground Floor");

        Slot slot = new Slot();
        slot.setId(1L);
        slot.setSlotNumber("A1");
        slot.setVehicleType(VehicleType.TWO_WHEELER);
        slot.setFloor(floor);

        when(slotRepository.findAll()).thenReturn(List.of(slot));

        List<SlotResponseDTO> slots = slotService.getAllSlots();
        assertEquals(1, slots.size());
        assertEquals("A1", slots.get(0).getSlotNumber());
    }
}
