package org.jamesrjacob.parkinglotreservation.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "parking.rates")
public class VehicleRateConfig {

    private Map<String, Double> rates = new HashMap<>();


    public VehicleRateConfig() {

        rates.put("TWO_WHEELER", 20.0);
        rates.put("FOUR_WHEELER", 30.0);
    }


    public Map<String, Double> getRates() {
        return rates;
    }

    public void setRates(Map<String, Double> rates) {
        this.rates = rates;
    }


    public Double getRateForVehicleType(String vehicleType) {
        return rates.getOrDefault(vehicleType.toUpperCase(), 0.0);
    }


    public void updateRate(String vehicleType, Double rate) {
        rates.put(vehicleType.toUpperCase(), rate);
    }


    public boolean containsVehicleType(String vehicleType) {
        return rates.containsKey(vehicleType.toUpperCase());
    }
}