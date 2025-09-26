package org.jamesrjacob.parkinglotreservation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jamesrjacob.parkinglotreservation.dto.FloorRequestDTO;
import org.jamesrjacob.parkinglotreservation.dto.FloorResponseDTO;
import org.jamesrjacob.parkinglotreservation.service.FloorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FloorController.class)
public class FloorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FloorService floorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateFloor_Success() throws Exception {
        FloorRequestDTO request = new FloorRequestDTO();
        request.setName("Ground Floor");

        FloorResponseDTO response = new FloorResponseDTO();
        response.setId(1L);
        response.setName("Ground Floor");

        when(floorService.saveFloor(org.mockito.ArgumentMatchers.any())).thenReturn(response);

        mockMvc.perform(post("/floors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Ground Floor"));
    }

    @Test
    void testGetFloors_Success() throws Exception {
        FloorResponseDTO floor1 = new FloorResponseDTO();
        floor1.setId(1L);
        floor1.setName("Ground Floor");

        FloorResponseDTO floor2 = new FloorResponseDTO();
        floor2.setId(2L);
        floor2.setName("First Floor");

        when(floorService.getAllFloors()).thenReturn(List.of(floor1, floor2));

        mockMvc.perform(get("/floors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }
}
