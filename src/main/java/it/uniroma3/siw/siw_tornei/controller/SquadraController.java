package it.uniroma3.siw.siw_tornei.controller;

import it.uniroma3.siw.siw_tornei.model.Squadra;
import it.uniroma3.siw.siw_tornei.service.GiocatoreService;
import it.uniroma3.siw.siw_tornei.service.SquadraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SquadraController {

    @Autowired
    private SquadraService squadraService;

    @Autowired
    private GiocatoreService giocatoreService;

    @GetMapping("/squadra")
    public String getSquadre(Model model) {
        model.addAttribute("squadre", this.squadraService.findAll());
        return "squadre";
    }

    @GetMapping("/squadra/{id}")
    public String getDettaglioSquadra(
            @PathVariable("id") Long id,
            @RequestParam(value = "from", required = false) String from,
            @RequestParam(value = "refId", required = false) Long refId,
            Model model) {

        Squadra squadra = this.squadraService.findById(id);
        if (squadra == null) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.NOT_FOUND, "Squadra non trovata");
        }
        model.addAttribute("squadra", squadra);
        model.addAttribute("giocatori", this.giocatoreService.findBySquadraOrderByCognomeAsc(squadra));
        // Passiamo alla vista i parametri di provenienza (se esistono)
        model.addAttribute("from", from);
        model.addAttribute("refId", refId);

        return "squadra";
    }
}