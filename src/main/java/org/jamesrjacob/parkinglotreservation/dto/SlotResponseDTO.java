package org.jamesrjacob.parkinglotreservation.dto;

import org.jamesrjacob.parkinglotreservation.model.VehicleType;

public class SlotResponseDTO {

    private Long id;
    private String slotNumber;
    private VehicleType vehicleType;
    private Long floorId;
    private String floorName;


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSlotNumber() { return slotNumber; }
    public void setSlotNumber(String slotNumber) { this.slotNumber = slotNumber; }

    public VehicleType getVehicleType() { return vehicleType; }
    public void setVehicleType(VehicleType vehicleType) { this.vehicleType = vehicleType; }

    public Long getFloorId() { return floorId; }
    public void setFloorId(Long floorId) { this.floorId = floorId; }

    public String getFloorName() { return floorName; }
    public void setFloorName(String floorName) { this.floorName = floorName; }
}
