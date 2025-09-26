package org.jamesrjacob.parkinglotreservation.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "parking.rates")
public class VehicleRateConfig {

    private Map<String, Double> rates = new HashMap<>();

    // Default constructor
    public VehicleRateConfig() {
        // Set default rates
        rates.put("TWO_WHEELER", 20.0);
        rates.put("FOUR_WHEELER", 30.0);
    }

    // Getters & Setters
    public Map<String, Double> getRates() {
        return rates;
    }

    public void setRates(Map<String, Double> rates) {
        this.rates = rates;
    }

    // Method to get rate for a vehicle type
    public Double getRateForVehicleType(String vehicleType) {
        return rates.getOrDefault(vehicleType.toUpperCase(), 0.0);
    }

    // Method to add or update a rate
    public void updateRate(String vehicleType, Double rate) {
        rates.put(vehicleType.toUpperCase(), rate);
    }

    // Method to check if vehicle type exists
    public boolean containsVehicleType(String vehicleType) {
        return rates.containsKey(vehicleType.toUpperCase());
    }
}