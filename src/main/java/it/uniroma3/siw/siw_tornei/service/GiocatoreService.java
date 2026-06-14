package it.uniroma3.siw.siw_tornei.service;

import it.uniroma3.siw.siw_tornei.model.Giocatore;
import it.uniroma3.siw.siw_tornei.model.Squadra;
import it.uniroma3.siw.siw_tornei.repository.GiocatoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GiocatoreService {

    @Autowired
    private GiocatoreRepository giocatoreRepository;

    @Autowired
    private SquadraService squadraService;

    /**
     * Salva un nuovo giocatore e lo associa a una squadra esistente.
     */
    @Transactional
    public void saveGiocatoreInSquadra(Giocatore giocatore, Long squadraId) {
        Squadra squadra = this.squadraService.findById(squadraId);
        if (squadra != null) {
            giocatore.setSquadra(squadra); // Imposta la relazione lato owning
            this.giocatoreRepository.save(giocatore);
        }
    }

    @Transactional(readOnly = true)
    public Giocatore findById(Long id) {
        return this.giocatoreRepository.findById(id).orElse(null);
    }

    @Transactional
    public void saveGiocatore(Giocatore giocatore) {
        this.giocatoreRepository.save(giocatore);
    }

    @Transactional
    public void deleteGiocatore(Long id) {
        this.giocatoreRepository.deleteById(id);
    }
}