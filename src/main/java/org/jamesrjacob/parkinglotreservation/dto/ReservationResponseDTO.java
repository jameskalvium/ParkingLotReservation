package org.jamesrjacob.parkinglotreservation.dto;

import org.jamesrjacob.parkinglotreservation.model.VehicleType;

import java.time.LocalDateTime;

public class ReservationResponseDTO {

    private Long id;
    private Long slotId;
    private String slotNumber;
    private VehicleType vehicleType;
    private String vehicleNumber;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double cost;

    // Getters and Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSlotId() { return slotId; }
    public void setSlotId(Long slotId) { this.slotId = slotId; }

    public String getSlotNumber() { return slotNumber; }
    public void setSlotNumber(String slotNumber) { this.slotNumber = slotNumber; }

    public VehicleType getVehicleType() { return vehicleType; }
    public void setVehicleType(VehicleType vehicleType) { this.vehicleType = vehicleType; }

    public String getVehicleNumber() { return vehicleNumber; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public Double getCost() { return cost; }
    public void setCost(Double cost) { this.cost = cost; }
}
