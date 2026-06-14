package it.uniroma3.siw.siw_tornei.controller;

import it.uniroma3.siw.siw_tornei.service.CredentialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private CredentialsService credentialsService;

    // --- Pagina di Login ---
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    // --- Pagina di Registrazione ---
    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    // --- Gestione della Registrazione ---
    @PostMapping("/register")
    public String registerUser(
            @RequestParam("nome") String nome,
            @RequestParam("cognome") String cognome,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            Model model) {

        // Delega validazione e salvataggio al service
        String errore = credentialsService.register(nome, cognome, email, password);

        if (errore != null) {
            // Validazione fallita: torna al form con il messaggio di errore
            // e i dati inseriti (tranne la password per sicurezza)
            model.addAttribute("error", errore);
            model.addAttribute("nome", nome);
            model.addAttribute("cognome", cognome);
            model.addAttribute("email", email);
            return "register";
        }

        // Registrazione completata con successo: redirect al login
        return "redirect:/login?registered";
    }
}