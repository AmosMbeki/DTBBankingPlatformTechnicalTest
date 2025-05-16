package com.amos.nyagar.account_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("/")  // Handles root path
    public String home() {
        return "Welcome to Accounts Service";
    }
}

