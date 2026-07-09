package it.uniroma3.siw.siw_tornei.service;

import it.uniroma3.siw.siw_tornei.model.Partita;
import it.uniroma3.siw.siw_tornei.model.Squadra;
import it.uniroma3.siw.siw_tornei.model.Torneo;
import it.uniroma3.siw.siw_tornei.repository.PartitaRepository;
import it.uniroma3.siw.siw_tornei.repository.SquadraRepository;
import it.uniroma3.siw.siw_tornei.repository.TorneoRepository;

import org.jspecify.annotations.Nullable;
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

    @Autowired
    private PartitaRepository partitaRepository;

    @Autowired
    private PartitaService partitaService;

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
            if (!squadra.getTornei().contains(torneo)) {
                squadra.getTornei().add(torneo);
                this.squadraRepository.save(squadra);
            }
        }
    }

    @Transactional
    public void removeSquadraFromTorneo(Long torneoId, Long squadraId) {
        Torneo torneo = this.torneoRepository.findById(torneoId).orElse(null);
        Squadra squadra = this.squadraRepository.findById(squadraId).orElse(null);

        if (torneo != null && squadra != null) {
            squadra.getTornei().remove(torneo);
            this.squadraRepository.save(squadra);
        }
    }

    @Transactional
    public void deleteTorneo(Long id) {
        Torneo torneo = this.torneoRepository.findById(id).orElse(null);
        if (torneo != null) {
            // 1. Delete all matches of this tournament
            List<Partita> partite = this.partitaRepository.findByTorneo(torneo);
            if (partite != null) {
                for (Partita p : partite) {
                    this.partitaService.deletePartita(p.getId());
                }
            }
            // 2. Remove the associations on the owning side (Squadra)
            for (Squadra s : torneo.getSquadre()) {
                s.getTornei().remove(torneo);
                this.squadraRepository.save(s);
            }
            // 3. Delete tournament itself
            this.torneoRepository.delete(torneo);
        }
    }

    @Transactional
    public void updateTorneo(Long id, Torneo torneoModificato) {
        Torneo torneo = this.torneoRepository.findById(id).orElse(null);
        if (torneo != null) {
            torneo.setNome(torneoModificato.getNome());
            torneo.setAnno(torneoModificato.getAnno());
            torneo.setDescrizione(torneoModificato.getDescrizione());
            this.torneoRepository.save(torneo);
        }
    }

    @Transactional(readOnly = true)
    public List<Torneo> findByNomeContainingIgnoreCase(String nome) {
        return this.torneoRepository.findByNomeContainingIgnoreCase(nome);
    }

    @Transactional(readOnly = true)
    public List<Torneo> findAllByOrderByAnnoAsc() {
        return this.torneoRepository.findAllByOrderByAnnoAsc();
    }

    @Transactional(readOnly = true)
    public Long countAllIscrizioniSquadre() {
        return this.torneoRepository.countAllIscrizioniSquadre();
    }




}