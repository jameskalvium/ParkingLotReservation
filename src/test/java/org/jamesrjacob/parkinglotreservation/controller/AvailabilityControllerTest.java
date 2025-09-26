package org.jamesrjacob.parkinglotreservation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jamesrjacob.parkinglotreservation.dto.SlotAvailabilityResponseDTO;
import org.jamesrjacob.parkinglotreservation.model.VehicleType;
import org.jamesrjacob.parkinglotreservation.service.AvailabilityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AvailabilityController.class)
class AvailabilityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AvailabilityService availabilityService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAvailableSlots_ShouldReturnList() throws Exception {
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(2);

        SlotAvailabilityResponseDTO dto = new SlotAvailabilityResponseDTO();
        dto.setSlotId(1L); dto.setSlotNumber("S1");

        when(availabilityService.getAvailableSlots(start, end, VehicleType.FOUR_WHEELER)).thenReturn(List.of(dto));

        mockMvc.perform(get("/availability")
                        .param("startTime", start.toString())
                        .param("endTime", end.toString())
                        .param("vehicleType", "CAR")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].slotNumber").value("S1"));
    }
}
