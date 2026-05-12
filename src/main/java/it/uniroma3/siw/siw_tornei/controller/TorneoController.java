package it.uniroma3.siw.siw_tornei.controller;

import it.uniroma3.siw.siw_tornei.model.Torneo;
import it.uniroma3.siw.siw_tornei.repository.TorneoRepository;
import it.uniroma3.siw.siw_tornei.service.TorneoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class TorneoController {

    @Autowired
    private TorneoService torneoService;

    @Autowired
    private TorneoRepository torneoRepository;

    @Autowired
    private it.uniroma3.siw.siw_tornei.service.ClassificaService classificaService;

    
    // Visualizzazione dell'elenco di tutti i tornei
    @GetMapping("/torneo")
    public String getTornei(Model model) {
        model.addAttribute("tornei", this.torneoService.findAll());
        return "tornei.html";
    }

    @GetMapping("/torneo/{id}")
    public String getTorneo(@PathVariable("id") Long id, Model model) {
        Torneo torneo = torneoRepository.findById(id).orElse(null);
        model.addAttribute("torneo", torneo);
        
        // Calcoliamo e passiamo la classifica calcolata dal service
        model.addAttribute("classifica", classificaService.generaClassifica(torneo));
        
        return "torneo.html";
    }
}