package org.jamesrjacob.parkinglotreservation.model;

public enum VehicleType {
    TWO_WHEELER(20.0),
    FOUR_WHEELER(30.0);

    private final double hourlyRate;

    VehicleType(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public static VehicleType fromString(String type) {
        try {
            return VehicleType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unsupported vehicle type: " + type);
        }
    }
}