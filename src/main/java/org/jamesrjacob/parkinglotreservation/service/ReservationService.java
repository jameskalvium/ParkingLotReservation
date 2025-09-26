package org.jamesrjacob.parkinglotreservation.service;

import org.jamesrjacob.parkinglotreservation.dto.ReservationRequestDTO;
import org.jamesrjacob.parkinglotreservation.dto.ReservationResponseDTO;
import org.jamesrjacob.parkinglotreservation.model.Reservation;
import org.jamesrjacob.parkinglotreservation.model.Slot;
import org.jamesrjacob.parkinglotreservation.repository.ReservationRepository;
import org.jamesrjacob.parkinglotreservation.repository.SlotRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final SlotRepository slotRepository;

    public ReservationService(ReservationRepository reservationRepository, SlotRepository slotRepository) {
        this.reservationRepository = reservationRepository;
        this.slotRepository = slotRepository;
    }

    @Transactional
    public ReservationResponseDTO reserveSlot(ReservationRequestDTO requestDTO) {
        Slot slot = slotRepository.findById(requestDTO.getSlotId())
                .orElseThrow(() -> new IllegalArgumentException("Slot not found"));


        boolean conflict = reservationRepository.existsBySlotAndTimeRange(
                slot, requestDTO.getStartTime(), requestDTO.getEndTime()
        );
        if (conflict) {
            throw new IllegalArgumentException("Slot is already reserved for this time");
        }


        long minutes = java.time.Duration.between(requestDTO.getStartTime(), requestDTO.getEndTime()).toMinutes();
        long hours = minutes / 60;
        if (minutes % 60 != 0) hours++;


        double cost = requestDTO.getVehicleType().getHourlyRate() * hours;

        // Create reservation
        Reservation reservation = new Reservation();
        reservation.setSlot(slot);
        reservation.setVehicleNumber(requestDTO.getVehicleNumber());
        reservation.setStartTime(requestDTO.getStartTime());
        reservation.setEndTime(requestDTO.getEndTime());
        reservation.setVehicleType(requestDTO.getVehicleType());
        reservation.setCost(cost);

        Reservation saved = reservationRepository.save(reservation);
        return convertToDTO(saved);
    }


    public Optional<ReservationResponseDTO> getReservation(Long id) {
        return reservationRepository.findById(id).map(this::convertToDTO);
    }

    public void cancelReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    public Page<ReservationResponseDTO> getAllReservations(Pageable pageable) {
        List<ReservationResponseDTO> allReservations = reservationRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), allReservations.size());

        return new PageImpl<>(allReservations.subList(start, end), pageable, allReservations.size());
    }

    private ReservationResponseDTO convertToDTO(Reservation reservation) {
        ReservationResponseDTO dto = new ReservationResponseDTO();
        dto.setId(reservation.getId());
        dto.setSlotId(reservation.getSlot().getId());
        dto.setSlotNumber(reservation.getSlot().getSlotNumber());
        dto.setVehicleType(reservation.getVehicleType());
        dto.setVehicleNumber(reservation.getVehicleNumber());
        dto.setStartTime(reservation.getStartTime());
        dto.setEndTime(reservation.getEndTime());
        dto.setCost(reservation.getCost());
        return dto;
    }
}
