package org.jamesrjacob.parkinglotreservation.dto;

import jakarta.validation.constraints.NotBlank;

public class FloorRequestDTO {
    @NotBlank(message = "Floor name is required")
    private String name;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
