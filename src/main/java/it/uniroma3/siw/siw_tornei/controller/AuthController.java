package it.uniroma3.siw.siw_tornei.controller;

import it.uniroma3.siw.siw_tornei.model.User;
import it.uniroma3.siw.siw_tornei.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 1. Mostra la pagina di registrazione
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // 2. Gestisce il salvataggio dei dati inviati dal form
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        // Controlliamo se lo username è già occupato
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            model.addAttribute("error", "Questo username è già registrato!");
            return "register";
        }

        // Criptiamo la password inserita a mano prima di salvarla
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Impostiamo i valori fissi per la registrazione standard
        user.setRuolo("USER");
        user.setProvider("LOCAL");

        // Salviamo l'utente nel DB
        userRepository.save(user);

        // Reindirizziamo al login con un parametro di successo opzionale
        return "redirect:/login";
    }
}