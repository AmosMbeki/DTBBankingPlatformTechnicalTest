package com.amos.nyagar.card_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("/")  // Handles root path
    public String home() {
        return "Welcome to Card Service";
    }
}
