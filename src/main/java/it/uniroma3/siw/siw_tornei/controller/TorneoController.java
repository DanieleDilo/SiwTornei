package it.uniroma3.siw.siw_tornei.controller;

import it.uniroma3.siw.siw_tornei.model.Torneo;
import it.uniroma3.siw.siw_tornei.service.TorneoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class TorneoController {

    @Autowired
    private TorneoService torneoService; //

    // Visualizzazione del dettaglio di un torneo specifico
    @GetMapping("/torneo/{id}")
    public String getTorneo(@PathVariable("id") Long id, Model model) {
        Torneo torneo = this.torneoService.findById(id); //
        model.addAttribute("torneo", torneo);
        return "torneo.html"; // Cercherà questo file in templates
    }

    // Visualizzazione dell'elenco di tutti i tornei
    @GetMapping("/torneo")
    public String getTornei(Model model) {
        model.addAttribute("tornei", this.torneoService.findAll());
        return "tornei.html";
    }
}