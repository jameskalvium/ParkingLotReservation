package org.jamesrjacob.parkinglotreservation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jamesrjacob.parkinglotreservation.dto.ReservationRequestDTO;
import org.jamesrjacob.parkinglotreservation.dto.ReservationResponseDTO;
import org.jamesrjacob.parkinglotreservation.model.VehicleType;
import org.jamesrjacob.parkinglotreservation.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void reserveSlot_ShouldReturnCreated() throws Exception {
        ReservationRequestDTO request = new ReservationRequestDTO();
        request.setSlotId(1L); request.setVehicleNumber("ABC123"); request.setVehicleType(VehicleType.FOUR_WHEELER);
        request.setStartTime(LocalDateTime.now().plusHours(1)); request.setEndTime(LocalDateTime.now().plusHours(2));

        ReservationResponseDTO response = new ReservationResponseDTO();
        response.setId(1L); response.setSlotNumber("S1"); response.setVehicleNumber("ABC123");

        when(reservationService.reserveSlot(any())).thenReturn(response);

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getReservation_ShouldReturnDTO() throws Exception {
        ReservationResponseDTO response = new ReservationResponseDTO(); response.setId(1L);
        when(reservationService.getReservation(1L)).thenReturn(Optional.of(response));

        mockMvc.perform(get("/reservations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }
}
