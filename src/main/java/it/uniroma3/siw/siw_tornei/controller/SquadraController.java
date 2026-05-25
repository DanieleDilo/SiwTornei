package it.uniroma3.siw.siw_tornei.controller;

import it.uniroma3.siw.siw_tornei.model.Squadra;
import it.uniroma3.siw.siw_tornei.repository.SquadraRepository;
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
    private SquadraRepository squadraRepository;
    

    @GetMapping("/squadra")
    public String getSquadre(Model model) {
        model.addAttribute("squadre", this.squadraService.findAll());
        return "squadre.html";
    }

    @GetMapping("/squadra/{id}")
    public String getDettaglioSquadra(
            @PathVariable("id") Long id,
            @RequestParam(value = "from", required = false) String from,
            @RequestParam(value = "refId", required = false) Long refId,
            Model model) {
            
        Squadra squadra = squadraRepository.findById(id).orElse(null);
        model.addAttribute("squadra", squadra);
        
        // Passiamo alla vista i parametri di provenienza (se esistono)
        model.addAttribute("from", from);
        model.addAttribute("refId", refId);
        
        return "squadra.html";
    }
}