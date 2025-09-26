package org.jamesrjacob.parkinglotreservation.service;

import org.jamesrjacob.parkinglotreservation.dto.SlotAvailabilityResponseDTO;
import org.jamesrjacob.parkinglotreservation.model.Slot;
import org.jamesrjacob.parkinglotreservation.model.VehicleType;
import org.jamesrjacob.parkinglotreservation.repository.SlotRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AvailabilityService {

    private final SlotRepository slotRepository;

    public AvailabilityService(SlotRepository slotRepository) {
        this.slotRepository = slotRepository;
    }

    public List<SlotAvailabilityResponseDTO> getAvailableSlots(LocalDateTime startTime, LocalDateTime endTime,
                                                               VehicleType vehicleType) {
        validateTimeRange(startTime, endTime);

        List<Slot> slots = (vehicleType == null)
                ? slotRepository.findAllAvailableSlotsByTimeRange(startTime, endTime)
                : slotRepository.findAvailableSlotsByVehicleTypeAndTimeRange(vehicleType, startTime, endTime);

        return slots.stream()
                .map(s -> convertToDTO(s, startTime, endTime))
                .collect(Collectors.toList());
    }

    public Page<SlotAvailabilityResponseDTO> getAvailableSlotsPaginated(LocalDateTime startTime, LocalDateTime endTime,
                                                                        VehicleType vehicleType, Pageable pageable) {
        List<SlotAvailabilityResponseDTO> allSlots = getAvailableSlots(startTime, endTime, vehicleType);


        Sort sort = pageable.getSort();
        if (sort.isSorted()) {
            for (Sort.Order order : sort) {
                Comparator<SlotAvailabilityResponseDTO> comparator = switch (order.getProperty()) {
                    case "slotNumber" -> Comparator.comparing(SlotAvailabilityResponseDTO::getSlotNumber);
                    case "floorName" -> Comparator.comparing(SlotAvailabilityResponseDTO::getFloorName);
                    case "availableFrom" -> Comparator.comparing(SlotAvailabilityResponseDTO::getAvailableFrom);
                    default -> Comparator.comparing(SlotAvailabilityResponseDTO::getSlotId);
                };
                if (order.isDescending()) comparator = comparator.reversed();
                allSlots.sort(comparator);
            }
        }


        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), allSlots.size());

        return new PageImpl<>(allSlots.subList(start, end), pageable, allSlots.size());
    }

    private void validateTimeRange(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) throw new IllegalArgumentException("Start time must be before end time");
        if (start.isBefore(LocalDateTime.now())) throw new IllegalArgumentException("Start time cannot be in the past");
    }

    private SlotAvailabilityResponseDTO convertToDTO(Slot slot, LocalDateTime start, LocalDateTime end) {
        SlotAvailabilityResponseDTO dto = new SlotAvailabilityResponseDTO();
        dto.setSlotId(slot.getId());
        dto.setSlotNumber(slot.getSlotNumber());
        dto.setVehicleType(slot.getVehicleType());
        dto.setFloorId(slot.getFloor().getId());
        dto.setFloorName(slot.getFloor().getName());
        dto.setAvailableFrom(start);
        dto.setAvailableUntil(end);
        return dto;
    }
}
