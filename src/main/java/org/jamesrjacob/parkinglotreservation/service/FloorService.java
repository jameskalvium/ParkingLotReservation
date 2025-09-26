package org.jamesrjacob.parkinglotreservation.service;

import org.jamesrjacob.parkinglotreservation.dto.FloorResponseDTO;
import org.jamesrjacob.parkinglotreservation.model.Floor;
import org.jamesrjacob.parkinglotreservation.repository.FloorRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class FloorService {

    private final FloorRepository floorRepository;

    public FloorService(FloorRepository floorRepository) {
        this.floorRepository = floorRepository;
    }

    public FloorResponseDTO saveFloor(Floor floor) {
        Floor saved = floorRepository.save(floor);
        return convertToDTO(saved);
    }

    public List<FloorResponseDTO> getAllFloors() {
        return floorRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public FloorResponseDTO getFloorById(Long id) {
        return floorRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }
    public Optional<Floor> getFloorEntityById(Long id) {
        return floorRepository.findById(id);
    }

    private FloorResponseDTO convertToDTO(Floor floor) {
        FloorResponseDTO dto = new FloorResponseDTO();
        dto.setId(floor.getId());
        dto.setName(floor.getName());
        return dto;
    }
}
