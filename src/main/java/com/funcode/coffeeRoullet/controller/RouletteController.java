package com.funcode.coffeeRoullet.controller;

import com.funcode.coffeeRoullet.model.Pairing;
import com.funcode.coffeeRoullet.model.User;
import com.funcode.coffeeRoullet.service.RouletteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roulette")
@CrossOrigin(origins = "*") // Allows your React app to connect during development
@Tag(name = "Coffee Roulette", description = "Endpoints for managing users and generating coffee matches")
public class RouletteController {

    @Autowired
    private RouletteService rouletteService;

    // Standard constructor injection
//    public RouletteController(RouletteService rouletteService) {
//        this.rouletteService = rouletteService;
//    }
    @Operation(summary = "Generate matches from an external list",
            description = "Pass a custom list of users to generate pairings without registering them first.")
    @PostMapping("/shuffle") // Changed to PostMapping as it accepts a Body
    public ResponseEntity<List<Pairing>> triggerShuffle(@RequestBody List<User> participants) {
        if (participants == null || participants.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(rouletteService.generateScalablePairs(participants));
    }

    @Operation(summary = "Match Registered Users",
            description = "Triggers the scalable pairing algorithm using all users currently in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully generated pairings",
                    content = @Content(schema = @Schema(implementation = Pairing.class))),
            @ApiResponse(responseCode = "204", description = "No users found to match")
    })
    @PostMapping("/match-current")
    public List<Pairing> matchCurrentUsers() {
        return rouletteService.generateScalablePairs(rouletteService.getRegisteredUsers());
    }

    @Operation(summary = "Register a User", description = "Add a user to the persistent/in-memory pool for weekly matching.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully registered"),
            @ApiResponse(responseCode = "400", description = "Invalid user data provided")
    })
    @PostMapping("/users")
    public String register(@RequestBody List<User> users) {
        users.forEach(rouletteService::addUser);
        return "Users registered successfully!";
    }
}



