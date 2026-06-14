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

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    public String newSquadra(@Valid @ModelAttribute("squadra") Squadra squadra, BindingResult bindingResult) {
        if (squadra.getNome() == null || squadra.getNome().trim().isEmpty()) {
            bindingResult.rejectValue("nome", "required", "Il nome del club è obbligatorio");
        }
        if (squadra.getCitta() == null || squadra.getCitta().trim().isEmpty()) {
            bindingResult.rejectValue("citta", "required", "La città è obbligatoria");
        }
        if (squadra.getAnnoFondazione() == null) {
            bindingResult.rejectValue("annoFondazione", "required", "L'anno di fondazione è obbligatorio");
        } else if (squadra.getAnnoFondazione() < 1800) {
            bindingResult.rejectValue("annoFondazione", "min", "L'anno di fondazione deve essere valido");
        }
        if (bindingResult.hasErrors()) {
            return "admin/formNewSquadra";
        }
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
    public String newTorneo(@Valid @ModelAttribute("torneo") Torneo torneo, BindingResult bindingResult) {
        if (torneo.getNome() == null || torneo.getNome().trim().isEmpty()) {
            bindingResult.rejectValue("nome", "required", "Il nome del torneo è obbligatorio");
        }
        if (torneo.getAnno() == null) {
            bindingResult.rejectValue("anno", "required", "L'anno del torneo è obbligatorio");
        } else if (torneo.getAnno() < 1800) {
            bindingResult.rejectValue("anno", "min", "L'anno deve essere valido");
        }
        if (bindingResult.hasErrors()) {
            return "admin/formNewTorneo";
        }
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
            @Valid @ModelAttribute("giocatore") Giocatore giocatore, BindingResult bindingResult, Model model) {
        if (giocatore.getNome() == null || giocatore.getNome().trim().isEmpty()) {
            bindingResult.rejectValue("nome", "required", "Il nome è obbligatorio");
        }
        if (giocatore.getCognome() == null || giocatore.getCognome().trim().isEmpty()) {
            bindingResult.rejectValue("cognome", "required", "Il cognome è obbligatorio");
        }
        if (giocatore.getRuolo() == null || giocatore.getRuolo().trim().isEmpty()) {
            bindingResult.rejectValue("ruolo", "required", "Il ruolo è obbligatorio");
        }
        if (giocatore.getDataNascita() == null) {
            bindingResult.rejectValue("dataNascita", "required", "La data di nascita è obbligatoria");
        } else if (giocatore.getDataNascita().isAfter(java.time.LocalDate.now())) {
            bindingResult.rejectValue("dataNascita", "past", "La data di nascita deve essere nel passato");
        }
        if (bindingResult.hasErrors()) {
            Squadra squadra = this.squadraService.findById(squadraId);
            model.addAttribute("squadra", squadra);
            return "admin/formNewGiocatore";
        }
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
    public String newPartita(@Valid @ModelAttribute("partita") Partita partita, BindingResult bindingResult, Model model) {
        if (partita.getTorneo() == null) {
            bindingResult.rejectValue("torneo", "required", "Il torneo è obbligatorio");
        }
        if (partita.getSquadraCasa() == null) {
            bindingResult.rejectValue("squadraCasa", "required", "La squadra di casa è obbligatoria");
        }
        if (partita.getSquadraTrasferta() == null) {
            bindingResult.rejectValue("squadraTrasferta", "required", "La squadra in trasferta è obbligatoria");
        } else if (partita.getSquadraCasa() != null && partita.getSquadraCasa().equals(partita.getSquadraTrasferta())) {
            bindingResult.rejectValue("squadraTrasferta", "duplicate", "La squadra di casa e quella in trasferta non possono essere la stessa");
        }
        if (partita.getDataOra() == null) {
            bindingResult.rejectValue("dataOra", "required", "La data e l'ora della partita sono obbligatorie");
        }
        if (partita.getLuogo() == null || partita.getLuogo().trim().isEmpty()) {
            bindingResult.rejectValue("luogo", "required", "Il luogo della partita è obbligatorio");
        }
        if (partita.getArbitro() == null) {
            bindingResult.rejectValue("arbitro", "required", "L'arbitro è obbligatorio");
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("tornei", this.torneoService.findAll());
            model.addAttribute("squadre", this.squadraService.findAll());
            model.addAttribute("arbitri", this.arbitroService.findAll());
            return "admin/formNewPartita";
        }
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
    public String updateRisultato(@PathVariable("id") Long id, @Valid @ModelAttribute("partita") Partita datiAggiornati, BindingResult bindingResult) {
        if (datiAggiornati.getGoalsHome() == null) {
            bindingResult.rejectValue("goalsHome", "required", "I gol della squadra in casa sono obbligatori");
        }
        if (datiAggiornati.getGoalsAway() == null) {
            bindingResult.rejectValue("goalsAway", "required", "I gol della squadra in trasferta sono obbligatori");
        }
        if (bindingResult.hasErrors()) {
            Partita partitaOriginale = this.partitaService.findById(id);
            if (partitaOriginale != null) {
                datiAggiornati.setTorneo(partitaOriginale.getTorneo());
                datiAggiornati.setSquadraCasa(partitaOriginale.getSquadraCasa());
                datiAggiornati.setSquadraTrasferta(partitaOriginale.getSquadraTrasferta());
            }
            return "admin/formUpdatePartita";
        }
        this.partitaService.updateRisultato(id, datiAggiornati);
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
    public String saveArbitro(@Valid @ModelAttribute("arbitro") Arbitro arbitro, BindingResult bindingResult) {
        if (arbitro.getNome() == null || arbitro.getNome().trim().isEmpty()) {
            bindingResult.rejectValue("nome", "required", "Il nome è obbligatorio");
        }
        if (arbitro.getCognome() == null || arbitro.getCognome().trim().isEmpty()) {
            bindingResult.rejectValue("cognome", "required", "Il cognome è obbligatorio");
        }
        if (arbitro.getCodiceArbitrale() == null || arbitro.getCodiceArbitrale().trim().isEmpty()) {
            bindingResult.rejectValue("codiceArbitrale", "required", "Il codice arbitrale è obbligatorio");
        }
        if (bindingResult.hasErrors()) {
            return "admin/formNewArbitro";
        }
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
    public String editTorneo(@PathVariable("id") Long id, @Valid @ModelAttribute("torneo") Torneo torneoModificato, BindingResult bindingResult) {
        if (torneoModificato.getNome() == null || torneoModificato.getNome().trim().isEmpty()) {
            bindingResult.rejectValue("nome", "required", "Il nome del torneo è obbligatorio");
        }
        if (torneoModificato.getAnno() == null) {
            bindingResult.rejectValue("anno", "required", "L'anno del torneo è obbligatorio");
        } else if (torneoModificato.getAnno() < 1800) {
            bindingResult.rejectValue("anno", "min", "L'anno deve essere valido");
        }
        if (bindingResult.hasErrors()) {
            torneoModificato.setId(id);
            return "admin/formEditTorneo";
        }
        this.torneoService.updateTorneo(id, torneoModificato);
        return "redirect:/torneo/" + id;
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
    public String editSquadra(@PathVariable("id") Long id, @Valid @ModelAttribute("squadra") Squadra squadraModificata, BindingResult bindingResult) {
        if (squadraModificata.getNome() == null || squadraModificata.getNome().trim().isEmpty()) {
            bindingResult.rejectValue("nome", "required", "Il nome del club è obbligatorio");
        }
        if (squadraModificata.getCitta() == null || squadraModificata.getCitta().trim().isEmpty()) {
            bindingResult.rejectValue("citta", "required", "La città è obbligatoria");
        }
        if (squadraModificata.getAnnoFondazione() == null) {
            bindingResult.rejectValue("annoFondazione", "required", "L'anno di fondazione è obbligatorio");
        } else if (squadraModificata.getAnnoFondazione() < 1800) {
            bindingResult.rejectValue("annoFondazione", "min", "L'anno di fondazione deve essere valido");
        }
        if (bindingResult.hasErrors()) {
            squadraModificata.setId(id);
            return "admin/formEditSquadra";
        }
        this.squadraService.updateSquadra(id, squadraModificata);
        return "redirect:/squadra/" + id;
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
    public String editGiocatore(@PathVariable("id") Long id, @Valid @ModelAttribute("giocatore") Giocatore giocatoreModificato, BindingResult bindingResult) {
        if (giocatoreModificato.getNome() == null || giocatoreModificato.getNome().trim().isEmpty()) {
            bindingResult.rejectValue("nome", "required", "Il nome è obbligatorio");
        }
        if (giocatoreModificato.getCognome() == null || giocatoreModificato.getCognome().trim().isEmpty()) {
            bindingResult.rejectValue("cognome", "required", "Il cognome è obbligatorio");
        }
        if (giocatoreModificato.getRuolo() == null || giocatoreModificato.getRuolo().trim().isEmpty()) {
            bindingResult.rejectValue("ruolo", "required", "Il ruolo è obbligatorio");
        }
        if (giocatoreModificato.getDataNascita() == null) {
            bindingResult.rejectValue("dataNascita", "required", "La data di nascita è obbligatoria");
        } else if (giocatoreModificato.getDataNascita().isAfter(java.time.LocalDate.now())) {
            bindingResult.rejectValue("dataNascita", "past", "La data di nascita deve essere nel passato");
        }
        if (giocatoreModificato.getAltezza() != null && giocatoreModificato.getAltezza() < 0) {
            bindingResult.rejectValue("altezza", "min", "L'altezza non può essere negativa");
        }
        if (bindingResult.hasErrors()) {
            Giocatore giocatoreOriginale = this.giocatoreService.findById(id);
            if (giocatoreOriginale != null) {
                giocatoreModificato.setSquadra(giocatoreOriginale.getSquadra());
                giocatoreModificato.setId(id);
            }
            return "admin/formEditGiocatore";
        }
        this.giocatoreService.updateGiocatore(id, giocatoreModificato);
        Giocatore giocatore = this.giocatoreService.findById(id);
        if (giocatore != null && giocatore.getSquadra() != null) {
            return "redirect:/squadra/" + giocatore.getSquadra().getId();
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