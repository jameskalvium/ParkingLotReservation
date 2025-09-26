package org.jamesrjacob.parkinglotreservation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jamesrjacob.parkinglotreservation.dto.SlotRequestDTO;
import org.jamesrjacob.parkinglotreservation.dto.SlotResponseDTO;
import org.jamesrjacob.parkinglotreservation.model.VehicleType;
import org.jamesrjacob.parkinglotreservation.service.SlotService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SlotController.class)
class SlotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SlotService slotService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createSlot_success() throws Exception {
        SlotRequestDTO request = new SlotRequestDTO();
        request.setSlotNumber("A1");
        request.setVehicleType(VehicleType.TWO_WHEELER);
        request.setFloorId(1L);

        SlotResponseDTO response = new SlotResponseDTO();
        response.setId(1L);
        response.setSlotNumber("A1");
        response.setVehicleType(VehicleType.TWO_WHEELER);
        response.setFloorId(1L);
        response.setFloorName("Ground Floor");

        when(slotService.createSlot(any(SlotRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/slots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.slotNumber").value("A1"))
                .andExpect(jsonPath("$.vehicleType").value("TWO_WHEELER"))
                .andExpect(jsonPath("$.floorName").value("Ground Floor"));
    }

    @Test
    void getSlots_returnsList() throws Exception {
        SlotResponseDTO slot1 = new SlotResponseDTO();
        slot1.setId(1L);
        slot1.setSlotNumber("A1");
        slot1.setVehicleType(VehicleType.TWO_WHEELER);
        slot1.setFloorId(1L);
        slot1.setFloorName("Ground Floor");

        SlotResponseDTO slot2 = new SlotResponseDTO();
        slot2.setId(2L);
        slot2.setSlotNumber("B1");
        slot2.setVehicleType(VehicleType.FOUR_WHEELER);
        slot2.setFloorId(2L);
        slot2.setFloorName("First Floor");

        when(slotService.getAllSlots()).thenReturn(List.of(slot1, slot2));

        mockMvc.perform(get("/slots")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].slotNumber").value("A1"))
                .andExpect(jsonPath("$[1].slotNumber").value("B1"));
    }
}
