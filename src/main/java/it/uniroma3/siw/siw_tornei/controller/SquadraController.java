package it.uniroma3.siw.siw_tornei.controller;

import it.uniroma3.siw.siw_tornei.model.Squadra;
import it.uniroma3.siw.siw_tornei.service.SquadraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class SquadraController {

    @Autowired
    private SquadraService squadraService;

    @GetMapping("/squadra")
    public String getSquadre(Model model) {
        model.addAttribute("squadre", this.squadraService.findAll());
        return "squadre.html";
    }

    @GetMapping("/squadra/{id}")
    public String getSquadra(@PathVariable("id") Long id, Model model) {
        Squadra squadra = this.squadraService.findById(id);
        model.addAttribute("squadra", squadra);
        return "squadra.html";
    }
}