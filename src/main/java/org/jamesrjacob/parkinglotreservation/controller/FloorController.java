package org.jamesrjacob.parkinglotreservation.controller;

import org.jamesrjacob.parkinglotreservation.dto.FloorRequestDTO;
import org.jamesrjacob.parkinglotreservation.dto.FloorResponseDTO;
import org.jamesrjacob.parkinglotreservation.model.Floor;
import org.jamesrjacob.parkinglotreservation.service.FloorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/floors")
public class FloorController {

    private final FloorService floorService;

    @Autowired
    public FloorController(FloorService floorService) {
        this.floorService = floorService;
    }

    @PostMapping
    public ResponseEntity<FloorResponseDTO> createFloor(@Valid @RequestBody FloorRequestDTO requestDTO) {
        Floor floor = new Floor();
        floor.setName(requestDTO.getName());

        FloorResponseDTO saved = floorService.saveFloor(floor);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<FloorResponseDTO>> getFloors() {
        return ResponseEntity.ok(floorService.getAllFloors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FloorResponseDTO> getFloor(@PathVariable Long id) {
        FloorResponseDTO floor = floorService.getFloorById(id);
        if (floor == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(floor);
    }
}
