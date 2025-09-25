package org.jamesrjacob.parkinglotreservation.service;

import org.jamesrjacob.parkinglotreservation.model.Floor;
import org.jamesrjacob.parkinglotreservation.repository.FloorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FloorService {

    private final FloorRepository floorRepository;

    public FloorService(FloorRepository floorRepository) {
        this.floorRepository = floorRepository;
    }

    public Floor createFloor(Floor floor) {
        return floorRepository.save(floor);
    }

    public List<Floor> getAllFloors() {
        return floorRepository.findAll();
    }
}
