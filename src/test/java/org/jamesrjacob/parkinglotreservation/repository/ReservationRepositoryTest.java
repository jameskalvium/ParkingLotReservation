package org.jamesrjacob.parkinglotreservation.repository;

import org.jamesrjacob.parkinglotreservation.model.Floor;
import org.jamesrjacob.parkinglotreservation.model.Reservation;
import org.jamesrjacob.parkinglotreservation.model.Slot;
import org.jamesrjacob.parkinglotreservation.model.VehicleType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect"
})
class ReservationRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    void findOverlappingReservations_NoOverlap_ShouldReturnEmpty() {

        Floor floor = new Floor();
        floor.setName("Ground Floor");
        Floor savedFloor = entityManager.persistAndFlush(floor);

        Slot slot = new Slot();
        slot.setSlotNumber("G-01");
        slot.setVehicleType(VehicleType.FOUR_WHEELER);
        slot.setFloor(savedFloor);
        Slot savedSlot = entityManager.persistAndFlush(slot);

        Reservation reservation = new Reservation();
        reservation.setVehicleNumber("KA01AB1234");
        reservation.setStartTime(LocalDateTime.of(2024, 1, 15, 10, 0));
        reservation.setEndTime(LocalDateTime.of(2024, 1, 15, 12, 0));
        reservation.setSlot(savedSlot);
        reservation.setCost(60.0);
        entityManager.persistAndFlush(reservation);


        List<Reservation> overlaps = reservationRepository.findOverlappingReservations(
                savedSlot.getId(),
                LocalDateTime.of(2024, 1, 15, 14, 0),
                LocalDateTime.of(2024, 1, 15, 16, 0)
        );


        assertTrue(overlaps.isEmpty());
    }

    @Test
    void findOverlappingReservations_WithOverlap_ShouldReturnReservations() {
        // Arrange
        Floor floor = new Floor();
        floor.setName("Ground Floor");
        Floor savedFloor = entityManager.persistAndFlush(floor);

        Slot slot = new Slot();
        slot.setSlotNumber("G-01");
        slot.setVehicleType(VehicleType.FOUR_WHEELER);
        slot.setFloor(savedFloor);
        Slot savedSlot = entityManager.persistAndFlush(slot);

        Reservation reservation = new Reservation();
        reservation.setVehicleNumber("KA01AB1234");
        reservation.setStartTime(LocalDateTime.of(2024, 1, 15, 10, 0));
        reservation.setEndTime(LocalDateTime.of(2024, 1, 15, 12, 0));
        reservation.setSlot(savedSlot);
        reservation.setCost(60.0);
        entityManager.persistAndFlush(reservation);


        List<Reservation> overlaps = reservationRepository.findOverlappingReservations(
                savedSlot.getId(),
                LocalDateTime.of(2024, 1, 15, 11, 0),
                LocalDateTime.of(2024, 1, 15, 13, 0)
        );


        assertEquals(1, overlaps.size());
        assertEquals("KA01AB1234", overlaps.get(0).getVehicleNumber());
    }
}