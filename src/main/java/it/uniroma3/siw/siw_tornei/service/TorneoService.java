package it.uniroma3.siw.siw_tornei.service;

import it.uniroma3.siw.siw_tornei.model.Torneo;
import it.uniroma3.siw.siw_tornei.repository.TorneoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TorneoService {

    @Autowired
    private TorneoRepository torneoRepository; //

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
     * Operazione di sola lettura: il docente consiglia di distinguere i livelli di isolamento.
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
}