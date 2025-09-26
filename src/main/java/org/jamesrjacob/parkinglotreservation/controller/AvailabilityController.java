package org.jamesrjacob.parkinglotreservation.controller;

import org.jamesrjacob.parkinglotreservation.dto.SlotAvailabilityResponseDTO;
import org.jamesrjacob.parkinglotreservation.model.VehicleType;
import org.jamesrjacob.parkinglotreservation.service.AvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/availability")
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    @Autowired
    public AvailabilityController(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    @GetMapping
    public ResponseEntity<List<SlotAvailabilityResponseDTO>> getAvailableSlots(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(required = false) VehicleType vehicleType) {


        if (startTime == null || endTime == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            List<SlotAvailabilityResponseDTO> availableSlots =
                    availabilityService.getAvailableSlots(startTime, endTime, vehicleType);
            return ResponseEntity.ok(availableSlots);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<SlotAvailabilityResponseDTO>> getAvailableSlotsPaginated(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(required = false) VehicleType vehicleType,
            Pageable pageable) {


        if (startTime == null || endTime == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Page<SlotAvailabilityResponseDTO> availableSlots =
                    availabilityService.getAvailableSlotsPaginated(startTime, endTime, vehicleType, pageable);
            return ResponseEntity.ok(availableSlots);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}