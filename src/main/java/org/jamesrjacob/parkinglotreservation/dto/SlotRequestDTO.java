package org.jamesrjacob.parkinglotreservation.dto;

import org.jamesrjacob.parkinglotreservation.model.VehicleType;
import jakarta.validation.constraints.NotNull;

public class SlotRequestDTO {

    @NotNull
    private String slotNumber;

    @NotNull
    private VehicleType vehicleType;

    @NotNull
    private Long floorId;

    // Getters and Setters
    public String getSlotNumber() { return slotNumber; }
    public void setSlotNumber(String slotNumber) { this.slotNumber = slotNumber; }

    public VehicleType getVehicleType() { return vehicleType; }
    public void setVehicleType(VehicleType vehicleType) { this.vehicleType = vehicleType; }

    public Long getFloorId() { return floorId; }
    public void setFloorId(Long floorId) { this.floorId = floorId; }
}
