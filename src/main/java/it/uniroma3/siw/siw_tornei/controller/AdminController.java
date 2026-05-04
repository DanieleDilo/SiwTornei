package it.uniroma3.siw.siw_tornei.controller;

import it.uniroma3.siw.siw_tornei.model.Torneo;
import it.uniroma3.siw.siw_tornei.service.TorneoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdminController {

    @Autowired
    private TorneoService torneoService;

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
}