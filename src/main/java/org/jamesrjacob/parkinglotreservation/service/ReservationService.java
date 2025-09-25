package org.jamesrjacob.parkinglotreservation.service;

import org.jamesrjacob.parkinglotreservation.dto.ReservationRequestDTO;
import org.jamesrjacob.parkinglotreservation.dto.ReservationResponseDTO;
import org.jamesrjacob.parkinglotreservation.exception.InvalidReservationException;
import org.jamesrjacob.parkinglotreservation.exception.SlotAlreadyBookedException;
import org.jamesrjacob.parkinglotreservation.exception.SlotNotFoundException;
import org.jamesrjacob.parkinglotreservation.model.Reservation;
import org.jamesrjacob.parkinglotreservation.model.Slot;
import org.jamesrjacob.parkinglotreservation.model.VehicleType;
import org.jamesrjacob.parkinglotreservation.repository.ReservationRepository;
import org.jamesrjacob.parkinglotreservation.repository.SlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final SlotRepository slotRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, SlotRepository slotRepository) {
        this.reservationRepository = reservationRepository;
        this.slotRepository = slotRepository;
    }

    @Transactional
    public ReservationResponseDTO reserveSlot(ReservationRequestDTO requestDTO) {
        validateReservationRequest(requestDTO);

        Slot slot = slotRepository.findById(requestDTO.getSlotId())
                .orElseThrow(() -> new SlotNotFoundException("Slot not found with id: " + requestDTO.getSlotId()));

        checkSlotAvailability(slot.getId(), requestDTO.getStartTime(), requestDTO.getEndTime(), null);

        Reservation reservation = new Reservation();
        reservation.setVehicleNumber(requestDTO.getVehicleNumber());
        reservation.setStartTime(requestDTO.getStartTime());
        reservation.setEndTime(requestDTO.getEndTime());
        reservation.setSlot(slot);
        reservation.setCost(calculateCost(slot.getVehicleType(), requestDTO.getStartTime(), requestDTO.getEndTime()));

        try {
            Reservation savedReservation = reservationRepository.save(reservation);
            return convertToDTO(savedReservation);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new SlotAlreadyBookedException("Slot was modified by another transaction. Please try again.");
        }
    }

    private void validateReservationRequest(ReservationRequestDTO request) {
        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new InvalidReservationException("Start time must be before end time");
        }

        Duration duration = Duration.between(request.getStartTime(), request.getEndTime());
        if (duration.toHours() > 24) {
            throw new InvalidReservationException("Reservation cannot exceed 24 hours");
        }

        if (request.getStartTime().isBefore(LocalDateTime.now())) {
            throw new InvalidReservationException("Start time cannot be in the past");
        }
    }

    private void checkSlotAvailability(Long slotId, LocalDateTime startTime, LocalDateTime endTime, Long excludeReservationId) {
        List<Reservation> overlappingReservations;

        if (excludeReservationId != null) {
            overlappingReservations = reservationRepository.findOverlappingReservationsExcludingCurrent(
                    slotId, startTime, endTime, excludeReservationId);
        } else {
            overlappingReservations = reservationRepository.findOverlappingReservations(
                    slotId, startTime, endTime);
        }

        if (!overlappingReservations.isEmpty()) {
            throw new SlotAlreadyBookedException("Slot is already booked for the requested time range");
        }
    }

    private double calculateCost(VehicleType vehicleType, LocalDateTime start, LocalDateTime end) {
        long minutes = Duration.between(start, end).toMinutes();
        long hours = (long) Math.ceil((double) minutes / 60);
        return hours * vehicleType.getHourlyRate();
    }

    public Optional<ReservationResponseDTO> getReservation(Long id) {
        return reservationRepository.findById(id).map(this::convertToDTO);
    }

    @Transactional
    public void cancelReservation(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new InvalidReservationException("Reservation not found with id: " + id);
        }
        reservationRepository.deleteById(id);
    }

    public Page<ReservationResponseDTO> getAllReservations(Pageable pageable) {
        return reservationRepository.findAll(pageable).map(this::convertToDTO);
    }

    private ReservationResponseDTO convertToDTO(Reservation reservation) {
        ReservationResponseDTO dto = new ReservationResponseDTO();
        dto.setId(reservation.getId());
        dto.setVehicleNumber(reservation.getVehicleNumber());
        dto.setStartTime(reservation.getStartTime());
        dto.setEndTime(reservation.getEndTime());
        dto.setCost(reservation.getCost());
        dto.setSlotId(reservation.getSlot().getId());
        dto.setSlotNumber(reservation.getSlot().getSlotNumber());
        dto.setFloorId(reservation.getSlot().getFloor().getId());
        dto.setFloorName(reservation.getSlot().getFloor().getName());
        return dto;
    }
}