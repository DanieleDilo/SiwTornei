package it.uniroma3.siw.siw_tornei.controller;

import it.uniroma3.siw.siw_tornei.service.GiocatoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GiocatoreController {

    @Autowired
    private GiocatoreService giocatoreService;

    @GetMapping("/giocatori")
    public String getGiocatori(@RequestParam(value = "ricerca", required = false) String ricerca, Model model) {
        if (ricerca != null && !ricerca.isBlank()) {
            model.addAttribute("giocatori", this.giocatoreService.searchGiocatori(ricerca.trim()));
        } else {
            model.addAttribute("giocatori", this.giocatoreService.findAll());
        }
        model.addAttribute("ricerca", ricerca != null ? ricerca : "");
        return "giocatori";
    }
}
