package it.uniroma3.siw.siw_tornei.controller;

import it.uniroma3.siw.siw_tornei.model.Giocatore;
import it.uniroma3.siw.siw_tornei.model.Partita;
import it.uniroma3.siw.siw_tornei.model.Squadra;
import it.uniroma3.siw.siw_tornei.model.Torneo;
import it.uniroma3.siw.siw_tornei.service.ArbitroService;
import it.uniroma3.siw.siw_tornei.service.GiocatoreService;
import it.uniroma3.siw.siw_tornei.service.PartitaService;
import it.uniroma3.siw.siw_tornei.service.SquadraService;
import it.uniroma3.siw.siw_tornei.service.TorneoService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdminController {

    @Autowired
    private TorneoService torneoService;

    @Autowired
    private SquadraService squadraService;

    @Autowired
    private GiocatoreService giocatoreService;

    @Autowired
    private PartitaService partitaService;
    
    @Autowired
    private ArbitroService arbitroService;

    @GetMapping("/admin/formNewSquadra")
    public String formNewSquadra(Model model) {
        model.addAttribute("squadra", new Squadra());
        return "admin/formNewSquadra";
    }

    @PostMapping("/admin/squadra")
    public String newSquadra(@ModelAttribute("squadra") Squadra squadra) {
        this.squadraService.saveSquadra(squadra);
        return "redirect:/squadra/" + squadra.getId();
    }

    // Mostra la pagina con le varie opzioni di amministrazione
    @GetMapping("/admin/index")
    public String indexAdmin() {
        return "admin/index";
    }

    // Carica la form per creare un nuovo torneo
    @GetMapping("/admin/formNewTorneo")
    public String formNewTorneo(Model model) {
        model.addAttribute("torneo", new Torneo());
        return "admin/formNewTorneo";
    }

    // Salva il torneo nel DB
    @PostMapping("/admin/torneo")
    public String newTorneo(@ModelAttribute("torneo") Torneo torneo) {
        this.torneoService.saveTorneo(torneo);
        return "redirect:/torneo/" + torneo.getId();
    }

    // 1. Mostra la pagina per gestire le iscrizioni di un torneo specifico
    @GetMapping("/admin/torneo/{id}/squadre")
    public String gestisciSquadreTorneo(@PathVariable("id") Long id, Model model) {
        Torneo torneo = this.torneoService.findById(id);
        // Recuperiamo tutte le squadre dal DB
        List<Squadra> tutteLeSquadre = this.squadraService.findAll();
        
        // Rimuoviamo dalla lista le squadre che sono già iscritte a questo torneo
        if(torneo.getSquadre() != null) {
            tutteLeSquadre.removeAll(torneo.getSquadre());
        }

        model.addAttribute("torneo", torneo);
        model.addAttribute("squadreDisponibili", tutteLeSquadre);
        return "admin/gestisciSquadreTorneo";
    }

    // 2. Azione vera e propria che iscrive la squadra
    @PostMapping("/admin/torneo/{torneoId}/squadra/{squadraId}")
    public String iscriviSquadra(@PathVariable("torneoId") Long torneoId, @PathVariable("squadraId") Long squadraId) {
        this.torneoService.addSquadraToTorneo(torneoId, squadraId);
        // Dopo aver salvato, ricarica la pagina di gestione
        return "redirect:/admin/torneo/" + torneoId + "/squadre";
    }

    // 1. Mostra la form (nota che ho cambiato {id} in {squadraId})
    @GetMapping("/admin/squadra/{squadraId}/formNewGiocatore")
    public String formNewGiocatore(@PathVariable("squadraId") Long squadraId, Model model) {
        Squadra squadra = this.squadraService.findById(squadraId);
        model.addAttribute("squadra", squadra);
        model.addAttribute("giocatore", new Giocatore());
        return "admin/formNewGiocatore";
    }

    // 2. Salva il giocatore (anche qui {id} diventa {squadraId})
    @PostMapping("/admin/squadra/{squadraId}/giocatore")
    public String newGiocatore(@PathVariable("squadraId") Long squadraId, @ModelAttribute("giocatore") Giocatore giocatore) {
        this.giocatoreService.saveGiocatoreInSquadra(giocatore, squadraId);
        return "redirect:/squadra/" + squadraId;
    }

    // 1. Mostra la form per pianificare una partita
    @GetMapping("/admin/formNewPartita")
    public String formNewPartita(Model model) {
        model.addAttribute("partita", new Partita());
        
        // Passiamo alla vista tutte le liste per popolare i menu a tendina!
        model.addAttribute("tornei", this.torneoService.findAll());
        model.addAttribute("squadre", this.squadraService.findAll());
        model.addAttribute("arbitri", this.arbitroService.findAll());
        
        return "admin/formNewPartita";
    }

    // 2. Salva la partita nel DB
    @PostMapping("/admin/partita")
    public String newPartita(@ModelAttribute("partita") Partita partita) {
        // Di default, una nuova partita è "SCHEDULED" (programmata)
        partita.setStato(it.uniroma3.siw.siw_tornei.model.StatoPartita.SCHEDULED);
        
        this.partitaService.savePartita(partita);
        return "redirect:/admin/index"; // Per ora torniamo alla home dell'admin
    }

    // 1. Mostra la form per inserire il risultato di una specifica partita
    @GetMapping("/admin/partita/{id}/risultato")
    public String formUpdateRisultato(@PathVariable("id") Long id, Model model) {
        Partita partita = this.partitaService.findById(id);
        model.addAttribute("partita", partita);
        return "admin/formUpdatePartita";
    }

    // 2. Salva il risultato nel DB e segna la partita come conclusa (PLAYED)
    @PostMapping("/admin/partita/{id}/risultato")
    public String updateRisultato(@PathVariable("id") Long id, @ModelAttribute("partita") Partita datiAggiornati) {
        Partita partitaEsistente = this.partitaService.findById(id);
        
        if (partitaEsistente != null) {
            partitaEsistente.setGoalsHome(datiAggiornati.getGoalsHome());
            partitaEsistente.setGoalsAway(datiAggiornati.getGoalsAway());
            partitaEsistente.setStato(it.uniroma3.siw.siw_tornei.model.StatoPartita.PLAYED); // Cambia stato!
            
            this.partitaService.savePartita(partitaEsistente);
        }
        
        return "redirect:/admin/index";
    }
}