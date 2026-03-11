package org.example.careplus01.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // Notice this is @Controller, NOT @RestController
public class WebController {

    @GetMapping("/login-page")
    public String loginPage() {
        return "login"; // Looks for src/main/resources/templates/login.html
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard"; // Looks for src/main/resources/templates/dashboard.html
    }
}