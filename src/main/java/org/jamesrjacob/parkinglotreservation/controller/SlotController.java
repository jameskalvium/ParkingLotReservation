package org.jamesrjacob.parkinglotreservation.controller;

import org.jamesrjacob.parkinglotreservation.model.Slot;
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
    public Slot createSlot(@RequestBody Slot slot) {
        return slotService.createSlot(slot);
    }

    @GetMapping
    public List<Slot> getSlots() {
        return slotService.getAllSlots();
    }
}
