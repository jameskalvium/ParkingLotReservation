package org.jamesrjacob.parkinglotreservation.service;

import org.jamesrjacob.parkinglotreservation.dto.SlotRequestDTO;
import org.jamesrjacob.parkinglotreservation.dto.SlotResponseDTO;
import org.jamesrjacob.parkinglotreservation.model.Floor;
import org.jamesrjacob.parkinglotreservation.model.Slot;
import org.jamesrjacob.parkinglotreservation.repository.FloorRepository;
import org.jamesrjacob.parkinglotreservation.repository.SlotRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SlotService {

    private final SlotRepository slotRepository;
    private final FloorRepository floorRepository;

    public SlotService(SlotRepository slotRepository, FloorRepository floorRepository) {
        this.slotRepository = slotRepository;
        this.floorRepository = floorRepository;
    }

    public SlotResponseDTO createSlot(SlotRequestDTO slotRequestDTO) {
        Floor floor = floorRepository.findById(slotRequestDTO.getFloorId())
                .orElseThrow(() -> new IllegalArgumentException("Floor not found"));

        Slot slot = new Slot();
        slot.setSlotNumber(slotRequestDTO.getSlotNumber());
        slot.setVehicleType(slotRequestDTO.getVehicleType());
        slot.setFloor(floor);

        Slot saved = slotRepository.save(slot);
        return convertToDTO(saved);
    }

    public List<SlotResponseDTO> getAllSlots() {
        return slotRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private SlotResponseDTO convertToDTO(Slot slot) {
        SlotResponseDTO dto = new SlotResponseDTO();
        dto.setId(slot.getId());
        dto.setSlotNumber(slot.getSlotNumber());
        dto.setVehicleType(slot.getVehicleType());
        dto.setFloorId(slot.getFloor().getId());
        dto.setFloorName(slot.getFloor().getName());
        return dto;
    }
}
