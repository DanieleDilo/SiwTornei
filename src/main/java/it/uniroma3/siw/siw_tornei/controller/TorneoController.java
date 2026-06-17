package it.uniroma3.siw.siw_tornei.controller;

import it.uniroma3.siw.siw_tornei.model.Torneo;
import it.uniroma3.siw.siw_tornei.service.TorneoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TorneoController {

    @Autowired
    private TorneoService torneoService;

    @Autowired
    private it.uniroma3.siw.siw_tornei.service.ClassificaService classificaService;

    // Visualizzazione dell'elenco di tutti i tornei
    @GetMapping("/torneo")
    public String getTornei(@RequestParam(value = "nome", required = false) String nome, Model model) {
        if (nome != null && !nome.isBlank()) {
            model.addAttribute("tornei", this.torneoService.findByNomeContainingIgnoreCase(nome));
        } else {
            model.addAttribute("tornei", this.torneoService.findAll());
        }
        model.addAttribute("nomeRicerca", nome != null ? nome : "");
        return "tornei";
    }

    @GetMapping("/torneo/{id}")
    public String getTorneo(@PathVariable("id") Long id, Model model) {
        Torneo torneo = this.torneoService.findById(id);
        if (torneo == null) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.NOT_FOUND, "Torneo non trovato");
        }
        model.addAttribute("torneo", torneo);

        // Calcoliamo e passiamo la classifica calcolata dal service
        model.addAttribute("classifica", classificaService.generaClassifica(torneo));

        return "torneo";
    }

}