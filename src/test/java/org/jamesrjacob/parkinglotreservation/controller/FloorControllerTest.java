package org.jamesrjacob.parkinglotreservation.controller;

import org.jamesrjacob.parkinglotreservation.model.Floor;
import org.jamesrjacob.parkinglotreservation.service.FloorService;
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

@WebMvcTest(FloorController.class)
class FloorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FloorService floorService;

    @Test
    void createFloor_ValidRequest_ShouldReturnCreatedFloor() throws Exception {
        // Arrange
        Floor floor = new Floor();
        floor.setName("Ground Floor");

        Floor savedFloor = new Floor();
        savedFloor.setId(1L);
        savedFloor.setName("Ground Floor");

        when(floorService.createFloor(any(Floor.class))).thenReturn(savedFloor);

        // Act & Assert
        mockMvc.perform(post("/floors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(floor)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Ground Floor"));
    }

    @Test
    void getFloors_ShouldReturnAllFloors() throws Exception {
        // Arrange
        Floor floor1 = new Floor();
        floor1.setId(1L);
        floor1.setName("Ground Floor");

        Floor floor2 = new Floor();
        floor2.setId(2L);
        floor2.setName("First Floor");

        when(floorService.getAllFloors()).thenReturn(List.of(floor1, floor2));

        // Act & Assert
        mockMvc.perform(get("/floors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }
}