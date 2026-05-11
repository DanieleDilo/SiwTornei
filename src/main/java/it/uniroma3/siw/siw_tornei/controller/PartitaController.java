package it.uniroma3.siw.siw_tornei.controller;

import it.uniroma3.siw.siw_tornei.service.PartitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PartitaController {

    @Autowired
    private PartitaService partitaService;

    // Mostra l'elenco di tutte le partite (calendario e risultati)
    @GetMapping("/partite")
    public String getPartite(Model model) {
        model.addAttribute("partite", this.partitaService.findAll());
        return "partite.html";
    }
}