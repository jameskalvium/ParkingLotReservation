package org.jamesrjacob.parkinglotreservation;

import org.jamesrjacob.parkinglotreservation.controller.*;
import org.jamesrjacob.parkinglotreservation.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ParkingLotReservationApplicationTests {

    @Autowired
    private ReservationController reservationController;

    @Autowired
    private AvailabilityController availabilityController;

    @Autowired
    private FloorController floorController;

    @Autowired
    private SlotController slotController;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private AvailabilityService availabilityService;

    @Autowired
    private FloorService floorService;

    @Autowired
    private SlotService slotService;

    @Test
    void contextLoads() {
        // Basic context loading test
    }

    @Test
    void allControllersAreLoaded() {
        assertNotNull(reservationController, "ReservationController should be loaded");
        assertNotNull(availabilityController, "AvailabilityController should be loaded");
        assertNotNull(floorController, "FloorController should be loaded");
        assertNotNull(slotController, "SlotController should be loaded");
    }

    @Test
    void allServicesAreLoaded() {
        assertNotNull(reservationService, "ReservationService should be loaded");
        assertNotNull(availabilityService, "AvailabilityService should be loaded");
        assertNotNull(floorService, "FloorService should be loaded");
        assertNotNull(slotService, "SlotService should be loaded");
    }

    @Test
    void mainApplicationStarts() {
        // Test that the application starts successfully
        ParkingLotReservationApplication.main(new String[] {});
        // If we reach here, the application started successfully
    }
}