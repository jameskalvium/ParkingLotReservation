package org.jamesrjacob.parkinglotreservation.service;

import org.jamesrjacob.parkinglotreservation.dto.FloorResponseDTO;
import org.jamesrjacob.parkinglotreservation.model.Floor;
import org.jamesrjacob.parkinglotreservation.repository.FloorRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FloorServiceTest {

    private final FloorRepository floorRepository = mock(FloorRepository.class);
    private final FloorService floorService = new FloorService(floorRepository);

    @Test
    void saveFloor_shouldReturnSavedFloor() {
        Floor floor = new Floor();
        floor.setId(1L);
        floor.setName("Ground Floor");

        when(floorRepository.save(floor)).thenReturn(floor);

        FloorResponseDTO dto = floorService.saveFloor(floor);
        assertNotNull(dto);
        assertEquals("Ground Floor", dto.getName());
    }

    @Test
    void getAllFloors_shouldReturnList() {
        Floor floor1 = new Floor();
        floor1.setId(1L);
        floor1.setName("Ground Floor");

        Floor floor2 = new Floor();
        floor2.setId(2L);
        floor2.setName("First Floor");

        when(floorRepository.findAll()).thenReturn(List.of(floor1, floor2));

        List<FloorResponseDTO> floors = floorService.getAllFloors();
        assertEquals(2, floors.size());
    }

    @Test
    void getFloorById_shouldReturnFloor_whenExists() {
        Floor floor = new Floor();
        floor.setId(1L);
        floor.setName("Ground Floor");

        when(floorRepository.findById(1L)).thenReturn(Optional.of(floor));

        FloorResponseDTO dto = floorService.getFloorById(1L);
        assertNotNull(dto);
        assertEquals("Ground Floor", dto.getName());
    }

    @Test
    void getFloorById_shouldReturnNull_whenNotFound() {
        when(floorRepository.findById(1L)).thenReturn(Optional.empty());
        assertNull(floorService.getFloorById(1L));
    }
}
