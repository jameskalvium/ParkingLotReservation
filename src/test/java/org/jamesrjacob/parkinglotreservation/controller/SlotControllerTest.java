package org.jamesrjacob.parkinglotreservation.controller;

import org.jamesrjacob.parkinglotreservation.model.Floor;
import org.jamesrjacob.parkinglotreservation.model.Slot;
import org.jamesrjacob.parkinglotreservation.model.VehicleType;
import org.jamesrjacob.parkinglotreservation.service.SlotService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SlotController.class)
class SlotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SlotService slotService;

    @Test
    void createSlot_ValidRequest_ShouldReturnCreatedSlot() throws Exception {
        // Arrange
        Floor floor = new Floor();
        floor.setId(1L);

        Slot slot = new Slot();
        slot.setSlotNumber("G-01");
        slot.setVehicleType(VehicleType.FOUR_WHEELER);
        slot.setFloor(floor);

        Slot savedSlot = new Slot();
        savedSlot.setId(1L);
        savedSlot.setSlotNumber("G-01");
        savedSlot.setVehicleType(VehicleType.FOUR_WHEELER);
        savedSlot.setFloor(floor);

        when(slotService.createSlot(any(Slot.class))).thenReturn(savedSlot);

        // Act & Assert
        mockMvc.perform(post("/slots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(slot)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.slotNumber").value("G-01"))
                .andExpect(jsonPath("$.vehicleType").value("FOUR_WHEELER"));
    }

    @Test
    void getSlots_ShouldReturnAllSlots() throws Exception {
        // Arrange
        Floor floor = new Floor();
        floor.setId(1L);

        Slot slot1 = new Slot();
        slot1.setId(1L);
        slot1.setSlotNumber("G-01");
        slot1.setVehicleType(VehicleType.FOUR_WHEELER);

        Slot slot2 = new Slot();
        slot2.setId(2L);
        slot2.setSlotNumber("G-02");
        slot2.setVehicleType(VehicleType.TWO_WHEELER);

        when(slotService.getAllSlots()).thenReturn(List.of(slot1, slot2));

        // Act & Assert
        mockMvc.perform(get("/slots"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].slotNumber").value("G-01"))
                .andExpect(jsonPath("$[1].slotNumber").value("G-02"));
    }
}