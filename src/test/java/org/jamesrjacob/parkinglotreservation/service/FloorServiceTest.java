package org.jamesrjacob.parkinglotreservation.service;

import org.jamesrjacob.parkinglotreservation.utils.TestDataFactory;
import org.jamesrjacob.parkinglotreservation.model.Floor;
import org.jamesrjacob.parkinglotreservation.repository.FloorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FloorServiceTest {

    @Mock
    private FloorRepository floorRepository;

    @InjectMocks
    private FloorService floorService;

    @Test
    void createFloor_ValidFloor_ShouldSaveAndReturn() {
        // Arrange
        Floor floor = TestDataFactory.createFloor(null, "Test Floor");
        Floor savedFloor = TestDataFactory.createFloor(1L, "Test Floor");

        when(floorRepository.save(any(Floor.class))).thenReturn(savedFloor);

        // Act
        Floor result = floorService.createFloor(floor);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Floor", result.getName());
        verify(floorRepository).save(floor);
    }

    @Test
    void getAllFloors_ShouldReturnAllFloors() {
        // Arrange
        Floor floor1 = TestDataFactory.createFloor(1L, "Floor 1");
        Floor floor2 = TestDataFactory.createFloor(2L, "Floor 2");

        when(floorRepository.findAll()).thenReturn(List.of(floor1, floor2));

        // Act
        List<Floor> result = floorService.getAllFloors();

        // Assert
        assertEquals(2, result.size());
        verify(floorRepository).findAll();
    }
}