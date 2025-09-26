package org.jamesrjacob.parkinglotreservation.service;

import org.jamesrjacob.parkinglotreservation.dto.SlotAvailabilityResponseDTO;
import org.jamesrjacob.parkinglotreservation.model.Floor;
import org.jamesrjacob.parkinglotreservation.model.Slot;
import org.jamesrjacob.parkinglotreservation.model.VehicleType;
import org.jamesrjacob.parkinglotreservation.repository.SlotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AvailabilityServiceTest {

    private SlotRepository slotRepository;
    private AvailabilityService availabilityService;

    @BeforeEach
    void setUp() {
        slotRepository = mock(SlotRepository.class);
        availabilityService = new AvailabilityService(slotRepository);
    }

    @Test
    void getAvailableSlots_returnsList() {
        Slot slot = new Slot();
        slot.setId(1L);
        slot.setSlotNumber("A1");
        slot.setVehicleType(VehicleType.TWO_WHEELER);
        Floor floor = new Floor();
        floor.setId(1L);
        floor.setName("Ground Floor");
        slot.setFloor(floor);

        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(2);

        when(slotRepository.findAllAvailableSlotsByTimeRange(start, end)).thenReturn(List.of(slot));

        List<SlotAvailabilityResponseDTO> slots = availabilityService.getAvailableSlots(start, end, null);

        assertEquals(1, slots.size());
        assertEquals("A1", slots.get(0).getSlotNumber());
    }

    @Test
    void getAvailableSlotsPaginated_returnsPage() {
        Slot slot = new Slot();
        slot.setId(1L);
        slot.setSlotNumber("A1");
        slot.setVehicleType(VehicleType.TWO_WHEELER);
        Floor floor = new Floor();
        floor.setId(1L);
        floor.setName("Ground Floor");
        slot.setFloor(floor);

        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(2);

        when(slotRepository.findAllAvailableSlotsByTimeRange(start, end)).thenReturn(List.of(slot));

        Pageable pageable = PageRequest.of(0, 10);
        var page = availabilityService.getAvailableSlotsPaginated(start, end, null, pageable);

        assertEquals(1, page.getTotalElements());
        assertEquals("A1", page.getContent().get(0).getSlotNumber());
    }
}
