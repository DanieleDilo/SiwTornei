package it.uniroma3.siw.siw_tornei.service;

import it.uniroma3.siw.siw_tornei.model.Partita;
import it.uniroma3.siw.siw_tornei.model.Giocatore;
import it.uniroma3.siw.siw_tornei.model.Squadra;
import it.uniroma3.siw.siw_tornei.repository.GiocatoreRepository;
import it.uniroma3.siw.siw_tornei.repository.PartitaRepository;
import it.uniroma3.siw.siw_tornei.repository.SquadraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SquadraService {

    @Autowired
    private SquadraRepository squadraRepository;

    @Autowired
    private GiocatoreRepository giocatoreRepository;

    @Autowired
    private PartitaRepository partitaRepository;

    @Autowired
    private PartitaService partitaService;

    @Transactional
    public void saveSquadra(Squadra squadra) {
        this.squadraRepository.save(squadra);
    }

    @Transactional(readOnly = true)
    public List<Squadra> findAll() {
        List<Squadra> squadre = new ArrayList<>();
        for (Squadra s : this.squadraRepository.findAll()) {
            squadre.add(s);
        }
        return squadre;
    }

    @Transactional(readOnly = true)
    public Squadra findById(Long id) {
        return this.squadraRepository.findById(id).orElse(null);
    }

    @Transactional
    public void deleteSquadra(Long id) {
        Squadra squadra = this.squadraRepository.findById(id).orElse(null);
        if (squadra != null) {
            // 1. Delete all matches of this team
            List<Partita> partite = this.partitaRepository.findBySquadra(squadra);
            if (partite != null) {
                for (Partita p : partite) {
                    this.partitaService.deletePartita(p.getId());
                }
            }
            // 2. Delete all players of this team
            if (squadra.getGiocatori() != null) {
                List<Giocatore> giocatoriDaEliminare = new ArrayList<>(squadra.getGiocatori());
                for (Giocatore g : giocatoriDaEliminare) {
                    this.giocatoreRepository.delete(g);
                }
            }
            // 3. Delete team
            this.squadraRepository.delete(squadra);
        }
    }

    @Transactional
    public void updateSquadra(Long id, Squadra squadraModificata) {
        Squadra squadra = this.squadraRepository.findById(id).orElse(null);
        if (squadra != null) {
            squadra.setNome(squadraModificata.getNome());
            squadra.setAnnoFondazione(squadraModificata.getAnnoFondazione());
            squadra.setCitta(squadraModificata.getCitta());
            this.squadraRepository.save(squadra);
        }
    }

    @Transactional(readOnly = true)
    public List<Squadra> findAllSorted() {
        return this.squadraRepository.findAllByOrderByAnnoFondazioneAscNomeAsc();
    }

    @Transactional(readOnly = true)
    public List<Squadra> findAllByNomeDesc() {
        return this.squadraRepository.findAllByOrderByNomeDesc();
    }

    @Transactional(readOnly = true)
    public List<Squadra> findByCitta(String citta) {
        return this.squadraRepository.findByCitta(citta);
    }

    @Transactional(readOnly = true)
    public List<Squadra> findAllByCittaDesc() {
        return this.squadraRepository.findAllByOrderByCittaDesc();
    }

}