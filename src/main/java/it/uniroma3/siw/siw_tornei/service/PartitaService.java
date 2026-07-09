package it.uniroma3.siw.siw_tornei.service;

import it.uniroma3.siw.siw_tornei.model.Commento;
import it.uniroma3.siw.siw_tornei.model.Partita;
import it.uniroma3.siw.siw_tornei.repository.CommentoRepository;
import it.uniroma3.siw.siw_tornei.repository.PartitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class PartitaService {
    @Autowired
    private PartitaRepository partitaRepository;

    @Autowired
    private CommentoRepository commentoRepository;

    @Transactional
    public void savePartita(Partita partita) {
        this.partitaRepository.save(partita);
    }

    @Transactional(readOnly = true)
    public List<Partita> findAll() {
        return (List<Partita>) this.partitaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Partita findById(Long id) {
        return this.partitaRepository.findById(id).orElse(null);
    }

    @Transactional
    public void deletePartita(Long id) {
        Partita partita = this.partitaRepository.findById(id).orElse(null);
        if (partita != null) {
            List<Commento> commenti = this.commentoRepository.findByPartitaIdOrderByDataCreazioneDesc(id);
            if (commenti != null && !commenti.isEmpty()) {
                this.commentoRepository.deleteAll(commenti);
            }
            this.partitaRepository.delete(partita);
        }
    }

    @Transactional
    public void updateRisultato(Long id, Partita datiAggiornati) {
        Partita partita = this.partitaRepository.findById(id).orElse(null);
        if (partita != null) {
            if (datiAggiornati.getStato() != null && datiAggiornati.getStato() == it.uniroma3.siw.siw_tornei.model.StatoPartita.SCHEDULED) {
                // Se lo stato è SCHEDULED, azzera il risultato
                partita.setGoalsHome(null);
                partita.setGoalsAway(null);
                partita.setStato(it.uniroma3.siw.siw_tornei.model.StatoPartita.SCHEDULED);
            } else {
                partita.setGoalsHome(datiAggiornati.getGoalsHome());
                partita.setGoalsAway(datiAggiornati.getGoalsAway());
                partita.setStato(it.uniroma3.siw.siw_tornei.model.StatoPartita.PLAYED);
            }
            this.partitaRepository.save(partita);
        }
    }

    @Transactional(readOnly = true)
    public List<Partita> findAllByOrderByDataDescOraDesc() {
        return this.partitaRepository.findAllByOrderByDataOraDesc();
    }

    @Transactional(readOnly = true)
    public List<Partita> findAllPlayed() {
        return this.partitaRepository.findAllPlayedByOrderByDataOraDesc();
    }

    @Transactional(readOnly = true)
    public List<Partita> findRandomPartite() {
        return this.partitaRepository.findRandomPartite(3);
    }

    @Transactional(readOnly = true)
    public long countGoalsByTorneoId(Long torneoId) {
        if(this.partitaRepository.countGoalsByTorneoId(torneoId) == null) {
            return 0L;
        }
        return this.partitaRepository.countGoalsByTorneoId(torneoId);
    }

}