package it.uniroma3.siw.siw_tornei.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.siw.siw_tornei.service.GiocatoreService;
import it.uniroma3.siw.siw_tornei.service.PartitaService;
import it.uniroma3.siw.siw_tornei.service.SquadraService;

@Controller
public class IndexController {

    @Autowired
    private SquadraService squadraService;



    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("squadreTotali", squadraService.findAll());
        return "index"; // Cerca un file chiamato index.html in templates
    }
}