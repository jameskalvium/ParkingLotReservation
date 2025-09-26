package org.jamesrjacob.parkinglotreservation.repository;

import org.jamesrjacob.parkinglotreservation.model.Floor;
import org.jamesrjacob.parkinglotreservation.model.Slot;
import org.jamesrjacob.parkinglotreservation.model.VehicleType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect"
})
class SlotRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SlotRepository slotRepository;

    @Test
    void findByFloorId_ShouldReturnSlotsForFloor() {
        // Arrange
        Floor floor1 = new Floor();
        floor1.setName("Ground Floor");
        Floor savedFloor1 = entityManager.persistAndFlush(floor1);

        Floor floor2 = new Floor();
        floor2.setName("First Floor");
        Floor savedFloor2 = entityManager.persistAndFlush(floor2);

        Slot slot1 = new Slot();
        slot1.setSlotNumber("G-01");
        slot1.setVehicleType(VehicleType.FOUR_WHEELER);
        slot1.setFloor(savedFloor1);
        entityManager.persistAndFlush(slot1);

        Slot slot2 = new Slot();
        slot2.setSlotNumber("F1-01");
        slot2.setVehicleType(VehicleType.TWO_WHEELER);
        slot2.setFloor(savedFloor2);
        entityManager.persistAndFlush(slot2);

        // Act
        List<Slot> slots = slotRepository.findByFloorId(savedFloor1.getId());

        // Assert
        assertEquals(1, slots.size());
        assertEquals("G-01", slots.get(0).getSlotNumber());
    }

    @Test
    void findByVehicleType_ShouldReturnSlotsForVehicleType() {
        // Arrange
        Floor floor = new Floor();
        floor.setName("Ground Floor");
        Floor savedFloor = entityManager.persistAndFlush(floor);

        Slot slot1 = new Slot();
        slot1.setSlotNumber("G-01");
        slot1.setVehicleType(VehicleType.FOUR_WHEELER);
        slot1.setFloor(savedFloor);
        entityManager.persistAndFlush(slot1);

        Slot slot2 = new Slot();
        slot2.setSlotNumber("G-02");
        slot2.setVehicleType(VehicleType.TWO_WHEELER);
        slot2.setFloor(savedFloor);
        entityManager.persistAndFlush(slot2);

        // Act
        List<Slot> slots = slotRepository.findByVehicleType(VehicleType.FOUR_WHEELER);

        // Assert
        assertEquals(1, slots.size());
        assertEquals(VehicleType.FOUR_WHEELER, slots.get(0).getVehicleType());
    }
}