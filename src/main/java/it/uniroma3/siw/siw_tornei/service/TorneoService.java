package it.uniroma3.siw.siw_tornei.service;

import it.uniroma3.siw.siw_tornei.model.Squadra;
import it.uniroma3.siw.siw_tornei.model.Torneo;
import it.uniroma3.siw.siw_tornei.repository.SquadraRepository;
import it.uniroma3.siw.siw_tornei.repository.TorneoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TorneoService {

    @Autowired
    private TorneoRepository torneoRepository;

    @Autowired
    private SquadraRepository squadraRepository;

    /**
     * Salva un nuovo torneo nel sistema.
     * Operazione di scrittura: richiede @Transactional.
     */
    @Transactional
    public void saveTorneo(Torneo torneo) {
        this.torneoRepository.save(torneo);
    }

    /**
     * Ritorna tutti i tornei.
     * Operazione di sola lettura
     */
    @Transactional(readOnly = true)
    public List<Torneo> findAll() {
        List<Torneo> tornei = new ArrayList<>();
        Iterable<Torneo> iterable = this.torneoRepository.findAll();
        for (Torneo t : iterable) {
            tornei.add(t);
        }
        return tornei;
    }

    @Transactional(readOnly = true)
    public Torneo findById(Long id) {
        return this.torneoRepository.findById(id).orElse(null);
    }

    /**
     * Iscrive una squadra a un torneo.
     * Salva la relazione aggiornando la squadra (lato owning della relazione).
     */
    @Transactional
    public void addSquadraToTorneo(Long torneoId, Long squadraId) {
        Torneo torneo = this.torneoRepository.findById(torneoId).orElse(null);
        Squadra squadra = this.squadraRepository.findById(squadraId).orElse(null);
        
        if (torneo != null && squadra != null) {
            // Aggiungiamo il torneo alla lista dei tornei della squadra
            squadra.getTornei().add(torneo);
            this.squadraRepository.save(squadra);
        }
    }
}