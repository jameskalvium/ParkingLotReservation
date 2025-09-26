package org.jamesrjacob.parkinglotreservation.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = VehicleRateConfig.class)
@EnableConfigurationProperties(VehicleRateConfig.class)
@TestPropertySource(properties = {
        "parking.rates.rates.TWO_WHEELER=20.0",
        "parking.rates.rates.FOUR_WHEELER=30.0",
        "parking.rates.rates.ELECTRIC=25.0"
})
class VehicleRateConfigTest {

    @Autowired
    private VehicleRateConfig vehicleRateConfig;

    @Test
    void getRateForVehicleType_ExistingType_ShouldReturnRate() {
        // Act
        Double rate = vehicleRateConfig.getRateForVehicleType("TWO_WHEELER");

        // Assert
        assertEquals(20.0, rate);
    }

    @Test
    void getRateForVehicleType_NonExistingType_ShouldReturnDefault() {
        // Act
        Double rate = vehicleRateConfig.getRateForVehicleType("UNKNOWN_TYPE");

        // Assert
        assertEquals(0.0, rate);
    }

    @Test
    void getRateForVehicleType_CaseInsensitive_ShouldWork() {
        // Act
        Double rate1 = vehicleRateConfig.getRateForVehicleType("two_wheeler");
        Double rate2 = vehicleRateConfig.getRateForVehicleType("TWO_WHEELER");

        // Assert
        assertEquals(20.0, rate1);
        assertEquals(20.0, rate2);
    }

    @Test
    void containsVehicleType_ExistingType_ShouldReturnTrue() {
        // Act & Assert
        assertTrue(vehicleRateConfig.containsVehicleType("FOUR_WHEELER"));
    }

    @Test
    void containsVehicleType_NonExistingType_ShouldReturnFalse() {
        // Act & Assert
        assertFalse(vehicleRateConfig.containsVehicleType("UNKNOWN_TYPE"));
    }

    @Test
    void updateRate_ShouldUpdateExistingRate() {
        // Arrange
        String vehicleType = "TWO_WHEELER";

        // Act
        vehicleRateConfig.updateRate(vehicleType, 25.0);
        Double updatedRate = vehicleRateConfig.getRateForVehicleType(vehicleType);

        // Assert
        assertEquals(25.0, updatedRate);
    }

    @Test
    void updateRate_NewType_ShouldAddNewRate() {
        // Arrange
        String newType = "BUS";

        // Act
        vehicleRateConfig.updateRate(newType, 60.0);
        Double newRate = vehicleRateConfig.getRateForVehicleType(newType);

        // Assert
        assertEquals(60.0, newRate);
        assertTrue(vehicleRateConfig.containsVehicleType(newType));
    }

    @Test
    void getAllRates_ShouldReturnAllConfiguredRates() {
        // Act
        var rates = vehicleRateConfig.getRates();

        // Assert
        assertNotNull(rates);
        assertEquals(20.0, rates.get("TWO_WHEELER"));
        assertEquals(30.0, rates.get("FOUR_WHEELER"));
        assertEquals(25.0, rates.get("ELECTRIC"));
    }
}