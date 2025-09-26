package org.jamesrjacob.parkinglotreservation.controller;

import org.jamesrjacob.parkinglotreservation.utils.TestDataFactory;
import org.jamesrjacob.parkinglotreservation.dto.SlotAvailabilityResponseDTO;
import org.jamesrjacob.parkinglotreservation.model.VehicleType;
import org.jamesrjacob.parkinglotreservation.service.AvailabilityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AvailabilityController.class)
class AvailabilityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AvailabilityService availabilityService;

    private final LocalDateTime startTime = TestDataFactory.futureStartTime();
    private final LocalDateTime endTime = TestDataFactory.futureEndTime();

    @Test
    void getAvailableSlots_ValidRequest_ShouldReturnSlots() throws Exception {
        // Arrange
        SlotAvailabilityResponseDTO slotDTO = new SlotAvailabilityResponseDTO();
        slotDTO.setSlotId(1L);
        slotDTO.setSlotNumber("G-01");
        slotDTO.setVehicleType(VehicleType.FOUR_WHEELER);
        slotDTO.setFloorId(1L);
        slotDTO.setFloorName("Ground Floor");
        slotDTO.setAvailableFrom(startTime);
        slotDTO.setAvailableUntil(endTime);

        when(availabilityService.getAvailableSlots(any(), any(), any()))
                .thenReturn(List.of(slotDTO));

        // Act & Assert
        mockMvc.perform(get("/availability")
                        .param("startTime", startTime.toString())
                        .param("endTime", endTime.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].slotId").value(1L))
                .andExpect(jsonPath("$[0].slotNumber").value("G-01"))
                .andExpect(jsonPath("$[0].vehicleType").value("FOUR_WHEELER"))
                .andExpect(jsonPath("$[0].floorId").value(1L))
                .andExpect(jsonPath("$[0].floorName").value("Ground Floor"));
    }

    @Test
    void getAvailableSlots_WithVehicleType_ShouldReturnFilteredSlots() throws Exception {
        // Arrange
        SlotAvailabilityResponseDTO slotDTO = new SlotAvailabilityResponseDTO();
        slotDTO.setSlotId(1L);
        slotDTO.setVehicleType(VehicleType.FOUR_WHEELER);

        when(availabilityService.getAvailableSlots(any(), any(), any()))
                .thenReturn(List.of(slotDTO));

        // Act & Assert
        mockMvc.perform(get("/availability")
                        .param("startTime", startTime.toString())
                        .param("endTime", endTime.toString())
                        .param("vehicleType", "FOUR_WHEELER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].vehicleType").value("FOUR_WHEELER"));
    }

    @Test
    void getAvailableSlots_InvalidTimeRange_ShouldReturnBadRequest() throws Exception {
        // Arrange - Mock service to throw exception for invalid time range
        when(availabilityService.getAvailableSlots(any(), any(), any()))
                .thenThrow(new IllegalArgumentException("Start time must be before end time"));

        // Act & Assert
        mockMvc.perform(get("/availability")
                        .param("startTime", endTime.toString()) // Start after end
                        .param("endTime", startTime.toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAvailableSlots_PastStartTime_ShouldReturnBadRequest() throws Exception {
        // Arrange - Mock service to throw exception for past time
        LocalDateTime pastTime = LocalDateTime.now().minusHours(1);
        when(availabilityService.getAvailableSlots(any(), any(), any()))
                .thenThrow(new IllegalArgumentException("Start time cannot be in the past"));

        // Act & Assert
        mockMvc.perform(get("/availability")
                        .param("startTime", pastTime.toString())
                        .param("endTime", endTime.toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAvailableSlots_MissingParameters_ShouldReturnBadRequest() throws Exception {
        // Act & Assert - Missing all required parameters
        mockMvc.perform(get("/availability"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAvailableSlots_MissingStartTime_ShouldReturnBadRequest() throws Exception {
        // Act & Assert - Missing startTime parameter
        mockMvc.perform(get("/availability")
                        .param("endTime", endTime.toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAvailableSlots_MissingEndTime_ShouldReturnBadRequest() throws Exception {
        // Act & Assert - Missing endTime parameter
        mockMvc.perform(get("/availability")
                        .param("startTime", startTime.toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAvailableSlots_ServiceReturnsEmptyList_ShouldReturnEmptyArray() throws Exception {
        // Arrange
        when(availabilityService.getAvailableSlots(any(), any(), any()))
                .thenReturn(List.of());

        // Act & Assert
        mockMvc.perform(get("/availability")
                        .param("startTime", startTime.toString())
                        .param("endTime", endTime.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getAvailableSlots_WithTwoWheelerFilter_ShouldReturnTwoWheelerSlots() throws Exception {
        // Arrange
        SlotAvailabilityResponseDTO slotDTO = new SlotAvailabilityResponseDTO();
        slotDTO.setSlotId(2L);
        slotDTO.setVehicleType(VehicleType.TWO_WHEELER);

        when(availabilityService.getAvailableSlots(any(), any(), any()))
                .thenReturn(List.of(slotDTO));

        // Act & Assert
        mockMvc.perform(get("/availability")
                        .param("startTime", startTime.toString())
                        .param("endTime", endTime.toString())
                        .param("vehicleType", "TWO_WHEELER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].vehicleType").value("TWO_WHEELER"));
    }

    @Test
    void getAvailableSlotsPaginated_ShouldReturnPaginatedResults() throws Exception {
        // Arrange
        SlotAvailabilityResponseDTO slotDTO = new SlotAvailabilityResponseDTO();
        slotDTO.setSlotId(1L);
        slotDTO.setSlotNumber("G-01");
        Page<SlotAvailabilityResponseDTO> page = new PageImpl<>(List.of(slotDTO));

        when(availabilityService.getAvailableSlotsPaginated(any(), any(), any(), any()))
                .thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/availability/paginated")
                        .param("startTime", startTime.toString())
                        .param("endTime", endTime.toString())
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].slotId").value(1L))
                .andExpect(jsonPath("$.content[0].slotNumber").value("G-01"));
    }

    @Test
    void getAvailableSlotsPaginated_WithVehicleType_ShouldReturnFilteredPaginatedResults() throws Exception {
        // Arrange
        SlotAvailabilityResponseDTO slotDTO = new SlotAvailabilityResponseDTO();
        slotDTO.setSlotId(1L);
        slotDTO.setVehicleType(VehicleType.FOUR_WHEELER);
        Page<SlotAvailabilityResponseDTO> page = new PageImpl<>(List.of(slotDTO));

        when(availabilityService.getAvailableSlotsPaginated(any(), any(), any(), any()))
                .thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/availability/paginated")
                        .param("startTime", startTime.toString())
                        .param("endTime", endTime.toString())
                        .param("vehicleType", "FOUR_WHEELER")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].vehicleType").value("FOUR_WHEELER"));
    }

    @Test
    void getAvailableSlotsPaginated_InvalidParameters_ShouldReturnBadRequest() throws Exception {
        // Arrange - Mock service to throw exception
        when(availabilityService.getAvailableSlotsPaginated(any(), any(), any(), any()))
                .thenThrow(new IllegalArgumentException("Invalid time range"));

        // Act & Assert
        mockMvc.perform(get("/availability/paginated")
                        .param("startTime", endTime.toString()) // Invalid: start after end
                        .param("endTime", startTime.toString())
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAvailableSlotsPaginated_MissingRequiredParameters_ShouldReturnBadRequest() throws Exception {
        // Act & Assert - Missing required time parameters
        mockMvc.perform(get("/availability/paginated")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAvailableSlots_WithInvalidTimeFormat_ShouldReturnBadRequest() throws Exception {
        // Act & Assert - Invalid time format
        mockMvc.perform(get("/availability")
                        .param("startTime", "invalid-date-time")
                        .param("endTime", endTime.toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAvailableSlots_WithInvalidVehicleType_ShouldUseNull() throws Exception {
        // Arrange
        SlotAvailabilityResponseDTO slotDTO = new SlotAvailabilityResponseDTO();
        slotDTO.setSlotId(1L);

        when(availabilityService.getAvailableSlots(any(), any(), any()))
                .thenReturn(List.of(slotDTO));

        // Act & Assert - Invalid vehicle type should be treated as null (all types)
        mockMvc.perform(get("/availability")
                        .param("startTime", startTime.toString())
                        .param("endTime", endTime.toString())
                        .param("vehicleType", "INVALID_TYPE")) // Invalid vehicle type
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].slotId").value(1L));
    }
}