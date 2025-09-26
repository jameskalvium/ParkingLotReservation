package org.jamesrjacob.parkinglotreservation.repository;

import org.jamesrjacob.parkinglotreservation.model.Floor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect"
})
class FloorRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FloorRepository floorRepository;

    @Test
    void findById_ExistingId_ShouldReturnFloor() {
        // Arrange
        Floor floor = new Floor();
        floor.setName("Ground Floor");
        Floor savedFloor = entityManager.persistAndFlush(floor);


        Optional<Floor> foundFloor = floorRepository.findById(savedFloor.getId());


        assertTrue(foundFloor.isPresent());
        assertEquals("Ground Floor", foundFloor.get().getName());
    }

    @Test
    void findById_NonExistingId_ShouldReturnEmpty() {

        Optional<Floor> foundFloor = floorRepository.findById(999L);


        assertFalse(foundFloor.isPresent());
    }

    @Test
    void save_NewFloor_ShouldPersist() {
        // Arrange
        Floor floor = new Floor();
        floor.setName("Test Floor");


        Floor savedFloor = floorRepository.save(floor);


        assertNotNull(savedFloor.getId());
        assertEquals("Test Floor", savedFloor.getName());


        Optional<Floor> foundFloor = floorRepository.findById(savedFloor.getId());
        assertTrue(foundFloor.isPresent());
    }
}