package org.jamesrjacob.parkinglotreservation.dto;

import org.jamesrjacob.parkinglotreservation.model.VehicleType;

public class SlotSummaryDTO {
    private Long id;
    private String slotNumber;
    private VehicleType vehicleType;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSlotNumber() { return slotNumber; }
    public void setSlotNumber(String slotNumber) { this.slotNumber = slotNumber; }

    public VehicleType getVehicleType() { return vehicleType; }
    public void setVehicleType(VehicleType vehicleType) { this.vehicleType = vehicleType; }
}