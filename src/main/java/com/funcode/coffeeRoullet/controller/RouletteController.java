package com.funcode.coffeeRoullet.controller;

import com.funcode.coffeeRoullet.model.Pairing;
import com.funcode.coffeeRoullet.model.User;
import com.funcode.coffeeRoullet.service.RouletteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roulette")
@CrossOrigin(origins = "*") // Allows your React app to connect during development
public class RouletteController {

    private final RouletteService rouletteService;

    // Standard constructor injection
    public RouletteController(RouletteService rouletteService) {
        this.rouletteService = rouletteService;
    }
    @GetMapping("/shuffle")
    public ResponseEntity<List<Pairing>> triggerShuffle(@RequestBody List<User> participants) {
        if (participants.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<Pairing> pairings = rouletteService.generateScalablePairs(participants);
        return ResponseEntity.ok(pairings);
    }

    /**
     * Alternative: If the service maintains its own in-memory list
     */
    @PostMapping("/match-current")
    public List<Pairing> matchCurrentUsers() {
        return rouletteService.generateScalablePairs(rouletteService.getRegisteredUsers());
    }
    @PostMapping("/users")
    public String register(@RequestBody User user) {
        rouletteService.addUser(user);
        return "Welcome to the Roulette, " + user.name() + "!";
    }
}



