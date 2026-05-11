package it.uniroma3.siw.siw_tornei.service;

import it.uniroma3.siw.siw_tornei.model.Squadra;
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
}