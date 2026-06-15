package it.uniroma3.siw.siw_tornei.controller;

import it.uniroma3.siw.siw_tornei.model.Credentials;
import it.uniroma3.siw.siw_tornei.model.User;
import it.uniroma3.siw.siw_tornei.service.CredentialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class UtenteController {

    @Autowired
    private CredentialsService credentialsService;

    @GetMapping("/utente/profilo")
    public String mostraProfilo(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        String username = authentication.getName();
        Optional<Credentials> credentialsOpt = credentialsService.findByUsername(username);

        if (credentialsOpt.isEmpty()) {
            return "redirect:/";
        }

        Credentials credentials = credentialsOpt.get();
        model.addAttribute("credentials", credentials);
        model.addAttribute("user", credentials.getUser());

        return "profiloUtente";
    }

    @PostMapping("/utente/profilo")
    public String aggiornaProfilo(
            @RequestParam("nome") String nome,
            @RequestParam("cognome") String cognome,
            Authentication authentication,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        String username = authentication.getName();
        Optional<Credentials> credentialsOpt = credentialsService.findByUsername(username);

        if (credentialsOpt.isEmpty()) {
            return "redirect:/";
        }

        Credentials credentials = credentialsOpt.get();

        // Validazione manuale dei campi
        if (nome == null || nome.isBlank()) {
            model.addAttribute("error", "Il nome è obbligatorio.");
            model.addAttribute("credentials", credentials);
            
            User tempUser = new User();
            tempUser.setNome(nome);
            tempUser.setCognome(cognome);
            model.addAttribute("user", tempUser);
            return "profiloUtente";
        }

        if (cognome == null || cognome.isBlank()) {
            model.addAttribute("error", "Il cognome è obbligatorio.");
            model.addAttribute("credentials", credentials);
            
            User tempUser = new User();
            tempUser.setNome(nome);
            tempUser.setCognome(cognome);
            model.addAttribute("user", tempUser);
            return "profiloUtente";
        }

        // Aggiorna tramite servizio
        credentialsService.updateProfile(credentials, nome, cognome);

        redirectAttributes.addFlashAttribute("success", "Profilo aggiornato con successo!");
        return "redirect:/utente/profilo";
    }
}
