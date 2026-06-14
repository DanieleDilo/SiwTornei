package it.uniroma3.siw.siw_tornei.controller;

import it.uniroma3.siw.siw_tornei.repository.CredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Rende disponibile il nome completo dell'utente loggato in tutti i template Thymeleaf,
 * sia per login locale che per login Google.
 */
@ControllerAdvice
public class GlobalModelAttributes {

    @Autowired
    private CredentialsRepository credentialsRepository;

    @ModelAttribute("nomeUtente")
    public String nomeUtente(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        String username = authentication.getName();

        return credentialsRepository.findByUsername(username)
                .filter(c -> c.getUser() != null)
                .map(c -> {
                    String nome = c.getUser().getNome();
                    String cognome = c.getUser().getCognome();
                    // Se nome e cognome sono vuoti, fallback all'email
                    if ((nome == null || nome.isBlank()) && (cognome == null || cognome.isBlank())) {
                        return username;
                    }
                    return (nome != null ? nome : "") + " " + (cognome != null ? cognome : "");
                })
                .orElse(username); // Fallback all'email se non troviamo le credenziali
    }
}
