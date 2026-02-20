package com.funcode.coffeeRoullet.config;

import com.funcode.coffeeRoullet.model.User;
import com.funcode.coffeeRoullet.service.RouletteService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoulletConfig {
    @Autowired
    RouletteService rouletteService;

    @PostConstruct
    public void loadStaticData() {
        rouletteService.addUser(new User("1", "Alice", "IT"));
        rouletteService.addUser(new User("2", "Bob", "HR"));
        rouletteService.addUser(new User("3", "Charlie", "Marketing"));
        rouletteService.addUser(new User("4", "David", "IT"));
        rouletteService.addUser(new User("5", "Eve", "IT"));
        rouletteService.addUser(new User("5", "Eve", "IT"));
        System.out.println("Static load complete: 5 users ready for roulette.");
    }
}
