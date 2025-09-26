package org.jamesrjacob.parkinglotreservation.controller;

import org.jamesrjacob.parkinglotreservation.utils.TestDataFactory;
import org.jamesrjacob.parkinglotreservation.dto.ReservationRequestDTO;
import org.jamesrjacob.parkinglotreservation.dto.ReservationResponseDTO;
import org.jamesrjacob.parkinglotreservation.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReservationService reservationService;

    private final LocalDateTime startTime = TestDataFactory.futureStartTime();
    private final LocalDateTime endTime = TestDataFactory.futureEndTime();

    @Test
    void reserveSlot_ValidRequest_ShouldReturnCreated() throws Exception {
        // Arrange
        ReservationRequestDTO requestDTO = TestDataFactory.createReservationRequestDTO(
                "KA01AB1234", startTime, endTime, 1L);

        ReservationResponseDTO responseDTO = new ReservationResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setVehicleNumber("KA01AB1234");
        responseDTO.setCost(60.0);

        when(reservationService.reserveSlot(any(ReservationRequestDTO.class))).thenReturn(responseDTO);

        // Act & Assert - Change expected status from 400 to 201
        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated()) // Changed from isBadRequest() to isCreated()
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.vehicleNumber").value("KA01AB1234"))
                .andExpect(jsonPath("$.cost").value(60.0));
    }

    // ... rest of the test methods remain the same ...
}