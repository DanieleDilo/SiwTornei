package it.uniroma3.siw.siw_tornei.service;

import it.uniroma3.siw.siw_tornei.model.Squadra;
import it.uniroma3.siw.siw_tornei.repository.SquadraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SquadraService {

    @Autowired
    private SquadraRepository squadraRepository;

    @Transactional
    public void saveSquadra(Squadra squadra) {
        this.squadraRepository.save(squadra);
    }

    @Transactional(readOnly = true)
    public Iterable<Squadra> findAll() {
        return this.squadraRepository.findAll();
    }
}