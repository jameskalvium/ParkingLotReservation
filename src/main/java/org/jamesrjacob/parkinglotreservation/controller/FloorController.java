package org.jamesrjacob.parkinglotreservation.controller;

import org.jamesrjacob.parkinglotreservation.model.Floor;
import org.jamesrjacob.parkinglotreservation.service.FloorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/floors")
public class FloorController {

    private final FloorService floorService;

    public FloorController(FloorService floorService) {
        this.floorService = floorService;
    }

    @PostMapping
    public Floor createFloor(@RequestBody Floor floor) {
        return floorService.createFloor(floor);
    }

    @GetMapping
    public List<Floor> getFloors() {
        return floorService.getAllFloors();
    }
}
