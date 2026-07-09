package it.uniroma3.siw.siw_tornei.service;

import it.uniroma3.siw.siw_tornei.model.Giocatore;
import it.uniroma3.siw.siw_tornei.model.Squadra;
import it.uniroma3.siw.siw_tornei.repository.GiocatoreRepository;

import java.util.List;

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

    @Transactional
    public void updateGiocatore(Long id, Giocatore giocatoreModificato) {
        Giocatore giocatore = this.giocatoreRepository.findById(id).orElse(null);
        if (giocatore != null) {
            giocatore.setNome(giocatoreModificato.getNome());
            giocatore.setCognome(giocatoreModificato.getCognome());
            giocatore.setDataNascita(giocatoreModificato.getDataNascita());
            giocatore.setRuolo(giocatoreModificato.getRuolo());
            giocatore.setAltezza(giocatoreModificato.getAltezza());
            this.giocatoreRepository.save(giocatore);
        }
    }

    @Transactional(readOnly = true)
    public List<Giocatore> findAllSorted() {
        return this.giocatoreRepository.findAllByOrderByCognomeDescNomeDesc();
    }

    @Transactional(readOnly = true)
    public List<Giocatore> searchGiocatori(String term) {
        if (term == null || term.isBlank()) {
            return this.findAllSorted();
        }
        return this.giocatoreRepository.searchByTerm(term);
    }

    @Transactional(readOnly = true)
    public List<Giocatore> findAll() {
        return this.giocatoreRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Giocatore> findBySquadraOrderByCognomeAsc(Squadra squadra) {
        return this.giocatoreRepository.findBySquadraOrderByCognomeAscNomeAsc(squadra.getId());
    }

    @Transactional(readOnly = true)
    public List<Giocatore> findAllByOrderByDataNascitaAsc() {
        return this.giocatoreRepository.findAllByOrderByDataNascitaAsc();
    }
}