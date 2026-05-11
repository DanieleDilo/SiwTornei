package it.uniroma3.siw.siw_tornei.service;

import it.uniroma3.siw.siw_tornei.model.Partita;
import it.uniroma3.siw.siw_tornei.repository.PartitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class PartitaService {
    @Autowired
    private PartitaRepository partitaRepository;

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
}