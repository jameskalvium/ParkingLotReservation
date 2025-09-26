package org.jamesrjacob.parkinglotreservation.controller;

import org.jamesrjacob.parkinglotreservation.dto.ReservationRequestDTO;
import org.jamesrjacob.parkinglotreservation.dto.ReservationResponseDTO;
import org.jamesrjacob.parkinglotreservation.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponseDTO> reserveSlot(@Valid @RequestBody ReservationRequestDTO requestDTO) {
        ReservationResponseDTO response = reservationService.reserveSlot(requestDTO);
        return ResponseEntity.created(URI.create("/reservations/" + response.getId())).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDTO> getReservation(@PathVariable Long id) {
        Optional<ReservationResponseDTO> reservation = reservationService.getReservation(id);
        return reservation.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Page<ReservationResponseDTO>> getAllReservations(Pageable pageable) {
        Page<ReservationResponseDTO> reservations = reservationService.getAllReservations(pageable);
        return ResponseEntity.ok(reservations);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return ResponseEntity.noContent().build();
    }
}
