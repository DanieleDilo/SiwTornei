package it.uniroma3.siw.siw_tornei.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    // Questo metodo intercetta la richiesta all'URL "/login"
    @GetMapping("/login")
    public String showLoginForm() {
        // Ritorna il nome del file HTML (Thymeleaf cercherà automaticamente "login.html" in templates)
        return "login"; 
    }
}