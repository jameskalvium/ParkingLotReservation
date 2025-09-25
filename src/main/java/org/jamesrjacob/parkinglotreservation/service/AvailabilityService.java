package org.jamesrjacob.parkinglotreservation.service;

import org.jamesrjacob.parkinglotreservation.dto.SlotAvailabilityResponseDTO;
import org.jamesrjacob.parkinglotreservation.model.Slot;
import org.jamesrjacob.parkinglotreservation.model.VehicleType;
import org.jamesrjacob.parkinglotreservation.repository.SlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AvailabilityService {

    private final SlotRepository slotRepository;

    @Autowired
    public AvailabilityService(SlotRepository slotRepository) {
        this.slotRepository = slotRepository;
    }

    public List<SlotAvailabilityResponseDTO> getAvailableSlots(LocalDateTime startTime, LocalDateTime endTime,
                                                               VehicleType vehicleType) {
        validateTimeRange(startTime, endTime);

        List<Slot> availableSlots;
        if (vehicleType != null) {
            availableSlots = slotRepository.findAvailableSlotsByVehicleTypeAndTimeRange(vehicleType, startTime, endTime);
        } else {
            availableSlots = slotRepository.findAllAvailableSlotsByTimeRange(startTime, endTime);
        }

        return availableSlots.stream()
                .map(slot -> convertToAvailabilityDTO(slot, startTime, endTime))
                .collect(Collectors.toList());
    }

    public Page<SlotAvailabilityResponseDTO> getAvailableSlotsPaginated(LocalDateTime startTime, LocalDateTime endTime,
                                                                        VehicleType vehicleType, Pageable pageable) {
        validateTimeRange(startTime, endTime);

        List<SlotAvailabilityResponseDTO> allSlots = getAvailableSlots(startTime, endTime, vehicleType);

        // Implement manual pagination
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), allSlots.size());

        return new PageImpl<>(
                allSlots.subList(start, end),
                pageable,
                allSlots.size()
        );
    }

    private void validateTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }
        if (startTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Start time cannot be in the past");
        }
    }

    private SlotAvailabilityResponseDTO convertToAvailabilityDTO(Slot slot, LocalDateTime availableFrom,
                                                                 LocalDateTime availableUntil) {
        SlotAvailabilityResponseDTO dto = new SlotAvailabilityResponseDTO();
        dto.setSlotId(slot.getId());
        dto.setSlotNumber(slot.getSlotNumber());
        dto.setVehicleType(slot.getVehicleType());
        dto.setFloorId(slot.getFloor().getId());
        dto.setFloorName(slot.getFloor().getName());
        dto.setAvailableFrom(availableFrom);
        dto.setAvailableUntil(availableUntil);
        return dto;
    }
}