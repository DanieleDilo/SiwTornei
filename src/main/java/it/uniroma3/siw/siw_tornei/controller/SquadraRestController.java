package it.uniroma3.siw.siw_tornei.controller;

import it.uniroma3.siw.siw_tornei.model.Squadra;
import it.uniroma3.siw.siw_tornei.repository.SquadraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class SquadraRestController {

    @Autowired
    private SquadraRepository squadraRepository;

    // Creiamo una classe "contenitore" interna (DTO) per evitare 
    // che Spring vada in loop infinito leggendo le relazioni con i giocatori/tornei
    class SquadraDTO {
        public Long id;
        public String nome;
        public String citta;
        public Integer annoFondazione;

        public SquadraDTO(Squadra s) {
            this.id = s.getId();
            this.nome = s.getNome();
            this.citta = s.getCitta();
            this.annoFondazione = s.getAnnoFondazione();
        }
    }

    @GetMapping("/squadre")
    public List<SquadraDTO> getAllSquadre() {
        List<SquadraDTO> risultato = new ArrayList<>();
        for (Squadra s : squadraRepository.findAll()) {
            risultato.add(new SquadraDTO(s));
        }
        return risultato;
    }
}