package it.uniroma3.siw.siw_tornei.controller;

import it.uniroma3.siw.siw_tornei.model.Arbitro;
import it.uniroma3.siw.siw_tornei.model.Giocatore;
import it.uniroma3.siw.siw_tornei.model.Partita;
import it.uniroma3.siw.siw_tornei.model.Squadra;
import it.uniroma3.siw.siw_tornei.model.Torneo;
import it.uniroma3.siw.siw_tornei.service.ArbitroService;
import it.uniroma3.siw.siw_tornei.service.GiocatoreService;
import it.uniroma3.siw.siw_tornei.service.PartitaService;
import it.uniroma3.siw.siw_tornei.service.SquadraService;
import it.uniroma3.siw.siw_tornei.service.TorneoService;
import it.uniroma3.siw.siw_tornei.service.PerformanceAnalysisService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @Autowired
    private PerformanceAnalysisService performanceAnalysisService;

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
        if (torneo.getSquadre() != null) {
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

    // 1. Mostra la form
    @GetMapping("/admin/squadra/{squadraId}/formNewGiocatore")
    public String formNewGiocatore(@PathVariable("squadraId") Long squadraId, Model model) {
        Squadra squadra = this.squadraService.findById(squadraId);
        model.addAttribute("squadra", squadra);
        model.addAttribute("giocatore", new Giocatore());
        return "admin/formNewGiocatore";
    }

    // 2. Salva il giocatore
    @PostMapping("/admin/squadra/{squadraId}/giocatore")
    public String newGiocatore(@PathVariable("squadraId") Long squadraId,
            @ModelAttribute("giocatore") Giocatore giocatore) {
        this.giocatoreService.saveGiocatoreInSquadra(giocatore, squadraId);
        return "redirect:/squadra/" + squadraId;
    }

    // 1. Mostra la form per pianificare una partita
    @GetMapping("/admin/formNewPartita")
    public String formNewPartita(Model model) {
        model.addAttribute("partita", new Partita());

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

    // --- ELIMINA TORNEO ---
    @PostMapping("/admin/torneo/{id}/delete")
    public String deleteTorneo(@PathVariable("id") Long id) {
        this.torneoService.deleteTorneo(id);
        return "redirect:/torneo"; // Torna alla lista pubblica dei tornei
    }

    // --- ELIMINA SQUADRA ---
    @PostMapping("/admin/squadra/{id}/delete")
    public String deleteSquadra(@PathVariable("id") Long id) {
        this.squadraService.deleteSquadra(id);
        return "redirect:/squadra"; // Torna alla lista pubblica
    }

    // --- ELIMINA GIOCATORE ---
    @PostMapping("/admin/giocatore/{id}/delete")
    public String deleteGiocatore(@PathVariable("id") Long id) {
        Giocatore giocatore = this.giocatoreService.findById(id);
        if (giocatore != null && giocatore.getSquadra() != null) {
            Long idSquadra = giocatore.getSquadra().getId();
            this.giocatoreService.deleteGiocatore(id);
            return "redirect:/squadra/" + idSquadra; // Torna alla pagina della squadra
        }
        this.giocatoreService.deleteGiocatore(id);
        return "redirect:/squadra";
    }

    // --- ELIMINA PARTITA ---
    @PostMapping("/admin/partita/{id}/delete")
    public String deletePartita(@PathVariable("id") Long id) {
        this.partitaService.deletePartita(id);
        return "redirect:/partite"; // Torna al calendario
    }

    // --- AGGIUNGI SQUADRA AL TORNEO ---
    @PostMapping("/admin/torneo/{id}/addSquadra")
    public String addSquadraToTorneo(@PathVariable("id") Long torneoId, @RequestParam("squadraId") Long squadraId) {
        this.torneoService.addSquadraToTorneo(torneoId, squadraId);
        return "redirect:/admin/torneo/" + torneoId + "/squadre";
    }

    // --- RIMUOVI SQUADRA DAL TORNEO ---
    @PostMapping("/admin/torneo/{torneoId}/removeSquadra/{squadraId}")
    public String removeSquadraFromTorneo(@PathVariable("torneoId") Long torneoId,
            @PathVariable("squadraId") Long squadraId) {
        this.torneoService.removeSquadraFromTorneo(torneoId, squadraId);
        return "redirect:/admin/torneo/" + torneoId + "/squadre";
    }

    // 1. Mostra il form per inserire un nuovo arbitro
    @GetMapping("/admin/formNewArbitro")
    public String formNewArbitro(Model model) {
        model.addAttribute("arbitro", new Arbitro());
        return "admin/formNewArbitro";
    }

    // 2. Salva l'arbitro compilato nel database
    @PostMapping("/admin/arbitro")
    public String saveArbitro(@ModelAttribute("arbitro") Arbitro arbitro) {
        this.arbitroService.saveArbitro(arbitro);
        return "redirect:/admin/index";
    }

    // --- MODIFICA TORNEO ---
    @GetMapping("/admin/torneo/{id}/edit")
    public String formEditTorneo(@PathVariable("id") Long id, Model model) {
        Torneo torneo = this.torneoService.findById(id);
        if (torneo == null) {
            return "redirect:/torneo";
        }
        model.addAttribute("torneo", torneo);
        return "admin/formEditTorneo";
    }

    @PostMapping("/admin/torneo/{id}/edit")
    public String editTorneo(@PathVariable("id") Long id, @ModelAttribute("torneo") Torneo torneoModificato) {
        Torneo torneo = this.torneoService.findById(id);
        if (torneo != null) {
            torneo.setNome(torneoModificato.getNome());
            torneo.setAnno(torneoModificato.getAnno());
            torneo.setDescrizione(torneoModificato.getDescrizione());
            this.torneoService.saveTorneo(torneo);
            return "redirect:/torneo/" + torneo.getId();
        }
        return "redirect:/torneo";
    }

    // --- MODIFICA SQUADRA ---
    @GetMapping("/admin/squadra/{id}/edit")
    public String formEditSquadra(@PathVariable("id") Long id, Model model) {
        Squadra squadra = this.squadraService.findById(id);
        if (squadra == null) {
            return "redirect:/squadra";
        }
        model.addAttribute("squadra", squadra);
        return "admin/formEditSquadra";
    }

    @PostMapping("/admin/squadra/{id}/edit")
    public String editSquadra(@PathVariable("id") Long id, @ModelAttribute("squadra") Squadra squadraModificata) {
        Squadra squadra = this.squadraService.findById(id);
        if (squadra != null) {
            squadra.setNome(squadraModificata.getNome());
            squadra.setAnnoFondazione(squadraModificata.getAnnoFondazione());
            squadra.setCitta(squadraModificata.getCitta());
            this.squadraService.saveSquadra(squadra);
            return "redirect:/squadra/" + squadra.getId();
        }
        return "redirect:/squadra";
    }

    // --- MODIFICA GIOCATORE ---
    @GetMapping("/admin/giocatore/{id}/edit")
    public String formEditGiocatore(@PathVariable("id") Long id, Model model) {
        Giocatore giocatore = this.giocatoreService.findById(id);
        if (giocatore == null) {
            return "redirect:/squadra";
        }
        model.addAttribute("giocatore", giocatore);
        return "admin/formEditGiocatore";
    }

    @PostMapping("/admin/giocatore/{id}/edit")
    public String editGiocatore(@PathVariable("id") Long id, @ModelAttribute("giocatore") Giocatore giocatoreModificato) {
        Giocatore giocatore = this.giocatoreService.findById(id);
        if (giocatore != null) {
            giocatore.setNome(giocatoreModificato.getNome());
            giocatore.setCognome(giocatoreModificato.getCognome());
            giocatore.setDataNascita(giocatoreModificato.getDataNascita());
            giocatore.setRuolo(giocatoreModificato.getRuolo());
            giocatore.setAltezza(giocatoreModificato.getAltezza());
            this.giocatoreService.saveGiocatore(giocatore);
            if (giocatore.getSquadra() != null) {
                return "redirect:/squadra/" + giocatore.getSquadra().getId();
            }
        }
        return "redirect:/squadra";
    }

    // --- ANALISI PERFORMANCE (Punto 8) ---
    @GetMapping("/admin/performance-analysis")
    public String performanceAnalysis(
            @RequestParam(value = "runBenchmarkId", required = false) Long runBenchmarkId,
            @RequestParam(value = "generated", required = false) Boolean generated,
            Model model) {
        
        List<Torneo> tornei = this.torneoService.findAll();
        model.addAttribute("tornei", tornei);
        model.addAttribute("generated", generated);

        if (runBenchmarkId != null) {
            java.util.Map<String, Object> benchmarkResult = this.performanceAnalysisService.eseguiBenchmark(runBenchmarkId);
            model.addAttribute("result", benchmarkResult);
            model.addAttribute("activeTorneoId", runBenchmarkId);
        }

        return "admin/performanceAnalysis";
    }

    @PostMapping("/admin/performance-analysis/generate-mock")
    public String generateMockData() {
        Long generatedId = this.performanceAnalysisService.generaDatiDiTest();
        return "redirect:/admin/performance-analysis?runBenchmarkId=" + generatedId + "&generated=true";
    }
}