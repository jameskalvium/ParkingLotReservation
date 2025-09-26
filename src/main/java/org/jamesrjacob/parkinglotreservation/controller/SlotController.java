package org.jamesrjacob.parkinglotreservation.controller;

import org.jamesrjacob.parkinglotreservation.dto.SlotRequestDTO;
import org.jamesrjacob.parkinglotreservation.dto.SlotResponseDTO;
import org.jamesrjacob.parkinglotreservation.service.SlotService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/slots")
public class SlotController {

    private final SlotService slotService;

    public SlotController(SlotService slotService) {
        this.slotService = slotService;
    }

    @PostMapping
    public SlotResponseDTO createSlot(@RequestBody SlotRequestDTO slotRequestDTO) {
        return slotService.createSlot(slotRequestDTO);
    }

    @GetMapping
    public List<SlotResponseDTO> getSlots() {
        return slotService.getAllSlots();
    }
}
