package it.uniroma3.siw.siw_tornei.controller;

import it.uniroma3.siw.siw_tornei.model.Commento;
import it.uniroma3.siw.siw_tornei.model.Partita;
import it.uniroma3.siw.siw_tornei.service.CommentoService;
import it.uniroma3.siw.siw_tornei.service.PartitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CommentoController {

    @Autowired
    private CommentoService commentoService;

    @Autowired
    private PartitaService partitaService;

    /**
     * Mostra il dettaglio di una partita con tutti i suoi commenti.
     * Accessibile a tutti (pubblico).
     */
    @GetMapping("/partita/{id}")
    public String dettaglioPartita(@PathVariable("id") Long id, Model model, Authentication authentication) {
        Partita partita = partitaService.findById(id);
        if (partita == null) {
            return "redirect:/partite";
        }

        List<Commento> commenti = commentoService.findByPartitaId(id);

        model.addAttribute("partita", partita);
        model.addAttribute("commenti", commenti);

        // Se l'utente è loggato, passiamo il suo username per capire quali commenti può modificare
        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("usernameCorrente", authentication.getName());
        }

        return "partita";
    }

    /**
     * Aggiunge un nuovo commento a una partita.
     * Richiede autenticazione (protetto da SecurityConfig).
     */
    @PostMapping("/commento/{partitaId}")
    public String aggiungiCommento(
            @PathVariable("partitaId") Long partitaId,
            @RequestParam("testo") String testo,
            Authentication authentication) {

        if (authentication != null) {
            commentoService.aggiungiCommento(partitaId, testo, authentication.getName());
        }

        return "redirect:/partita/" + partitaId;
    }

    /**
     * Mostra il form per modificare un commento.
     * Solo l'autore del commento può accedere.
     */
    @GetMapping("/commento/{commentoId}/edit")
    public String formEditCommento(
            @PathVariable("commentoId") Long commentoId,
            Model model,
            Authentication authentication) {

        Commento commento = commentoService.findById(commentoId);

        if (commento == null) {
            return "redirect:/partite";
        }

        // Verifica che l'utente sia l'autore
        if (authentication == null || !commento.getAutore().getUsername().equals(authentication.getName())) {
            return "redirect:/partita/" + commento.getPartita().getId();
        }

        model.addAttribute("commento", commento);
        return "formEditCommento";
    }

    /**
     * Salva le modifiche a un commento.
     * Solo l'autore del commento può modificarlo.
     */
    @PostMapping("/commento/{commentoId}/edit")
    public String editCommento(
            @PathVariable("commentoId") Long commentoId,
            @RequestParam("testo") String testo,
            Authentication authentication) {

        Commento commento = commentoService.findById(commentoId);
        Long partitaId = (commento != null && commento.getPartita() != null)
                ? commento.getPartita().getId() : null;

        if (authentication != null) {
            commentoService.modificaCommento(commentoId, testo, authentication.getName());
        }

        return "redirect:/partita/" + (partitaId != null ? partitaId : "");
    }

    /**
     * Elimina un commento.
     * Solo l'autore del commento può eliminarlo.
     */
    @PostMapping("/commento/{commentoId}/delete")
    public String eliminaCommento(
            @PathVariable("commentoId") Long commentoId,
            Authentication authentication) {

        Commento commento = commentoService.findById(commentoId);
        Long partitaId = (commento != null && commento.getPartita() != null)
                ? commento.getPartita().getId() : null;

        if (authentication != null) {
            commentoService.eliminaCommento(commentoId, authentication.getName());
        }

        return "redirect:/partita/" + (partitaId != null ? partitaId : "");
    }
}
